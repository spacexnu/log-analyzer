package br.com.adlermedrado.loganalyzer.security;

import br.com.adlermedrado.loganalyzer.domain.metrics.MetricName;
import br.com.adlermedrado.loganalyzer.util.Logging;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class RCEDetector {

  private final Counter rceCounter;

  private static final Logging log = new Logging(RCEDetector.class);
  private static final Pattern RCE_PATTERN =
      Pattern.compile("(?i)(exec\\(|system\\(|runtime\\.exec|processbuilder|cmd.exe|/bin/sh)");

  public RCEDetector(MeterRegistry meterRegistry) {
    rceCounter =
        Counter.builder(MetricName.RCE_DETECTED.getMetricName())
            .description("Counts detected RCE attempts")
            .register(meterRegistry);
  }

  public void analyzeLog(String logEntry) {
    if (RCE_PATTERN.matcher(logEntry).find()) {
      rceCounter.increment();
      log.warn("Possible RCE attack detected: " + logEntry);
    }
  }
}
