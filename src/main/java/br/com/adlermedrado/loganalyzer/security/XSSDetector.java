package br.com.adlermedrado.loganalyzer.security;

import br.com.adlermedrado.loganalyzer.domain.metrics.MetricName;
import br.com.adlermedrado.loganalyzer.util.Logging;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class XSSDetector {

  private final Counter xssCounter;

  private static final Logging log = new Logging(XSSDetector.class);
  private static final Pattern XSS_PATTERN =
      Pattern.compile("(?i)<script|onerror|alert\\(|document\\.cookie|eval\\(|onload=");

  public XSSDetector(MeterRegistry meterRegistry) {
    xssCounter =
        Counter.builder(MetricName.XSS_DETECTED.getMetricName())
            .description("Counts detected XSS attempts")
            .register(meterRegistry);
  }

  public void analyzeLog(String logEntry) {
    if (XSS_PATTERN.matcher(logEntry).find()) {
      xssCounter.increment();
      log.warn("Possible XSS attack detected: " + logEntry);
    }
  }
}
