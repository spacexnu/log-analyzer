package br.com.adlermedrado.loganalyzer.security;

import br.com.adlermedrado.loganalyzer.domain.metrics.MetricName;
import br.com.adlermedrado.loganalyzer.util.Logging;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class PathTraversalDetector {

  private final Counter pathTraversalCounter;

  private static final Logging log = new Logging(PathTraversalDetector.class);

  private static final Pattern PATH_TRAVERSAL_PATTERN =
      Pattern.compile("(?i)\\.\\./|/etc/passwd|/etc/shadow|c:\\\\windows\\\\system32");

  public PathTraversalDetector(MeterRegistry meterRegistry) {
    this.pathTraversalCounter =
        Counter.builder(MetricName.PATH_TRAVERSAL_DETECTED.getMetricName())
            .description("Counts detected path traversal attempts")
            .register(meterRegistry);
  }

  public void analyzeLog(String logEntry) {
    if (PATH_TRAVERSAL_PATTERN.matcher(logEntry).find()) {
      pathTraversalCounter.increment();
      log.warn("Possible Path Traversal attack detected: " + logEntry);
    }
  }
}
