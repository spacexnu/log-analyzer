package br.com.adlermedrado.loganalyzer.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import br.com.adlermedrado.loganalyzer.domain.metrics.MetricName;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RCEDetectorTest {

  private RCEDetector rceDetector;
  private MeterRegistry meterRegistry;

  @BeforeEach
  void setUp() {
    meterRegistry = new SimpleMeterRegistry();
    rceDetector = new RCEDetector(meterRegistry);
  }

  @Test
  void testAnalyzeLog() {
    String logMessage = "Executing command: runtime.exec('cmd.exe')";
    rceDetector.analyzeLog(logMessage);
    assertEquals(1, meterRegistry.get(MetricName.RCE_DETECTED.getMetricName()).counter().count());
  }
}
