package br.com.adlermedrado.loganalyzer.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import br.com.adlermedrado.loganalyzer.domain.metrics.MetricName;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PathTraversalDetectorTest {

  private PathTraversalDetector pathTraversalDetector;
  private MeterRegistry meterRegistry;

  @BeforeEach
  void setUp() {
    meterRegistry = new SimpleMeterRegistry();
    pathTraversalDetector = new PathTraversalDetector(meterRegistry);
  }

  @Test
  void testAnalyzeLog() {
    String logMessage = "Accessing ../../etc/passwd";
    pathTraversalDetector.analyzeLog(logMessage);
    assertEquals(
        1, meterRegistry.get(MetricName.PATH_TRAVERSAL_DETECTED.getMetricName()).counter().count());
  }
}
