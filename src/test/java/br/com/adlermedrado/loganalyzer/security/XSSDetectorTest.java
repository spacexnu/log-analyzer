package br.com.adlermedrado.loganalyzer.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.adlermedrado.loganalyzer.domain.metrics.MetricName;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class XSSDetectorTest {

  private XSSDetector xssDetector;
  private MeterRegistry meterRegistry;

  @BeforeEach
  void setUp() {
    meterRegistry = new SimpleMeterRegistry();
    xssDetector = new XSSDetector(meterRegistry);
  }

  @Test
  void testAnalyzeLog() {
    String logMessage = "<script>alert('XSS');</script>";
    xssDetector.analyzeLog(logMessage);
    assertEquals(1, meterRegistry.get(MetricName.XSS_DETECTED.getMetricName()).counter().count());
  }
}
