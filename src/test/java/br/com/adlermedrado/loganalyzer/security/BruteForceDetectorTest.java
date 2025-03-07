package br.com.adlermedrado.loganalyzer.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import br.com.adlermedrado.loganalyzer.domain.metrics.MetricName;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BruteForceDetectorTest {

  private BruteForceDetector bruteForceDetector;
  private MeterRegistry meterRegistry;

  @BeforeEach
  void setUp() {
    meterRegistry = new SimpleMeterRegistry();
    bruteForceDetector = new BruteForceDetector(meterRegistry);
  }

  @Test
  void testAnalyzeLog() {
    String logMessage = "Failed login from IP: 192.168.1.1";
    bruteForceDetector.analyzeLog(logMessage);
    assertEquals(
        1, meterRegistry.get(MetricName.BRUTE_FORCE_DETECTED.getMetricName()).counter().count());
  }
}
