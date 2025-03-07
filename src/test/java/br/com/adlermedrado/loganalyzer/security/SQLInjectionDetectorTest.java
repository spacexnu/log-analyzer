package br.com.adlermedrado.loganalyzer.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.adlermedrado.loganalyzer.domain.metrics.MetricName;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SQLInjectionDetectorTest {

  private SQLInjectionDetector sqlInjectionDetector;
  private MeterRegistry meterRegistry;

  @BeforeEach
  void setUp() {
    meterRegistry = new SimpleMeterRegistry();
    sqlInjectionDetector = new SQLInjectionDetector(meterRegistry);
  }

  @Test
  void testAnalyzeLog() {
    String logMessage = "SELECT * FROM users WHERE id = 1";
    sqlInjectionDetector.analyzeLog(logMessage);
    assertEquals(
        1, meterRegistry.get(MetricName.SQL_INJECTION_DETECTED.getMetricName()).counter().count());
  }
}
