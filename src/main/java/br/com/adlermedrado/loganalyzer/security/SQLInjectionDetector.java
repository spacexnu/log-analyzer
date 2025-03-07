package br.com.adlermedrado.loganalyzer.security;

import br.com.adlermedrado.loganalyzer.domain.metrics.MetricName;
import br.com.adlermedrado.loganalyzer.util.Logging;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SQLInjectionDetector {

  private final Counter sqlInjectionCounter;

  private final Logging log = new Logging(SQLInjectionDetector.class);

  private static final List<String> SQL_INJECTION_PATTERNS =
      List.of(
          "SELECT * FROM",
          "DROP TABLE",
          "UNION SELECT",
          "OR 1=1",
          "--",
          "EXEC xp_cmdshell",
          "INFORMATION_SCHEMA",
          "INSERT INTO",
          "UPDATE SET",
          "DELETE FROM",
          "HAVING 1=1",
          "xp_cmdshell",
          "' OR '1'='1",
          "\" OR \"1\"=\"1",
          "' OR 1=1 --");

  public SQLInjectionDetector(MeterRegistry meterRegistry) {
    sqlInjectionCounter =
        Counter.builder(MetricName.SQL_INJECTION_DETECTED.getMetricName())
            .description("Counts detected SQL Injections attempts")
            .register(meterRegistry);
  }

  public void analyzeLog(String logMessage) {
    for (String pattern : SQL_INJECTION_PATTERNS) {
      if (logMessage.toUpperCase().contains(pattern)) {
        sqlInjectionCounter.increment();
        log.warn("Possible SQL Injection attempt detected: " + logMessage);
        return;
      }
    }
  }
}
