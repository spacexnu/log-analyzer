package br.com.adlermedrado.loganalyzer.security;

import br.com.adlermedrado.loganalyzer.domain.metrics.MetricName;
import br.com.adlermedrado.loganalyzer.util.Logging;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BruteForceDetector {

  private static final Logging log = new Logging(BruteForceDetector.class);

  private final Map<String, List<Instant>> failedLogins = new ConcurrentHashMap<>();
  private final Counter bruteForceCounter;

  @Value("${brute.force.max.attempts:5}")
  private int maxAttempts;

  @Value("${brute.force.time.window:60}")
  private int timeWindowSeconds;

  public BruteForceDetector(MeterRegistry meterRegistry) {
    bruteForceCounter =
        Counter.builder(MetricName.BRUTE_FORCE_DETECTED.getMetricName())
            .description("Counts detected brute force attempts")
            .register(meterRegistry);
  }

  public void analyzeLog(String logMessage) {
    Optional<String> ip = extractIpFromLog(logMessage);
    if (ip.isPresent() && isLoginFailure(logMessage)) {
      trackFailedAttempt(ip.get());
    }
  }

  private void trackFailedAttempt(String ip) {
    List<Instant> attempts = failedLogins.computeIfAbsent(ip, k -> new ArrayList<>());
    attempts.add(Instant.now());

    attempts.removeIf(attempt -> attempt.isBefore(Instant.now().minusSeconds(timeWindowSeconds)));

    if (attempts.size() >= maxAttempts) {
      bruteForceCounter.increment();
      log.warn("Possible brute-force attack detected from IP: " + ip);
    }
  }

  private boolean isLoginFailure(String logMessage) {
    return logMessage.contains("Failed login") || logMessage.contains("Invalid credentials");
  }

  private Optional<String> extractIpFromLog(String logMessage) {
    String[] words = logMessage.split("\\s+");
    for (String word : words) {
      if (word.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b")) {
        return Optional.of(word);
      }
    }
    return Optional.empty();
  }
}
