package br.com.adlermedrado.loganalyzer.service;

import br.com.adlermedrado.loganalyzer.cache.LogCache;
import br.com.adlermedrado.loganalyzer.client.loki.LokiQueryException;
import br.com.adlermedrado.loganalyzer.client.loki.LokiQueryResponse;
import br.com.adlermedrado.loganalyzer.security.*;
import br.com.adlermedrado.loganalyzer.util.Logging;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LogAnalysisService {
  private static final Logging log = new Logging(LogAnalysisService.class);
  private final LokiService lokiService;
  private final BruteForceDetector bruteForceDetector;
  private final SQLInjectionDetector sqlInjectionDetector;
  private final XSSDetector xssDetector;
  private final RCEDetector rceDetector;
  private final PathTraversalDetector pathTraversalDetector;
  private final LogCache logCache;

  private final List<String> monitoredApps;
  private final int logLimit;
  private final ExecutorService executorService;

  private Instant lastQueryTime;

  private static final int MESSAGE_INDEX = 1;

  public LogAnalysisService(
      LokiService lokiService,
      BruteForceDetector bruteForceDetector,
      SQLInjectionDetector sqlInjectionDetector,
      XSSDetector xssDetector,
      RCEDetector rceDetector,
      PathTraversalDetector pathTraversalDetector,
      LogCache logCache,
      @Value("${monitored.apps}") String monitoredApps,
      @Value("${log.limit}") int logLimit,
      @Value("${log.analysis.threads:4}") int threadPoolSize) {
    this.lokiService = lokiService;
    this.bruteForceDetector = bruteForceDetector;
    this.sqlInjectionDetector = sqlInjectionDetector;
    this.xssDetector = xssDetector;
    this.rceDetector = rceDetector;
    this.pathTraversalDetector = pathTraversalDetector;
    this.logCache = logCache;
    this.monitoredApps = Arrays.asList(monitoredApps.split(","));
    this.logLimit = logLimit;
    this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    this.lastQueryTime = Instant.now().minus(10, ChronoUnit.MINUTES);
  }

  public void analyzeLogs() {
    Instant now = Instant.now();
    long start = lastQueryTime.toEpochMilli() * 1_000_000;
    long end = now.toEpochMilli() * 1_000_000;

    log.info("Querying logs from {} to {}", lastQueryTime, now);

    for (String app : monitoredApps) {
      log.info("Fetching logs for app: " + app);

      String query = String.format("{app=\"%s\"} | json | line_format \"{{.message}}\"", app);

      try {
        LokiQueryResponse response = lokiService.queryLogs(query, logLimit, start, end);
        List<String> logs = extractMessages(response.data().result());

        for (String logEntry : logs) {
          if (!logCache.isDuplicate(logEntry)) {
            executorService.submit(
                () -> {
                  bruteForceDetector.analyzeLog(logEntry);
                  sqlInjectionDetector.analyzeLog(logEntry);
                  xssDetector.analyzeLog(logEntry);
                  rceDetector.analyzeLog(logEntry);
                  pathTraversalDetector.analyzeLog(logEntry);
                });
          } else {
            log.debug(
                "Skipping duplicate log entry: {}",
                logEntry.length() > 100 ? logEntry.substring(0, 100) + "..." : logEntry);
          }
        }

      } catch (LokiQueryException e) {
        log.error("Error querying logs from Loki for app {}: {}", app, e.getMessage(), e);
      }
    }

    log.info("Log analysis completed successfully, updating lastQueryTime.");
    lastQueryTime = now;
  }

  private List<String> extractMessages(List<LokiQueryResponse.Result> results) {
    return results.stream()
        .flatMap(result -> result.values().stream().map(value -> value.get(MESSAGE_INDEX)))
        .toList();
  }
}
