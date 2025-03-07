package br.com.adlermedrado.loganalyzer.metric;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class LogMetricsService {

  private final AtomicInteger totalLogsAnalyzed;
  private final AtomicInteger bruteForceAttempts;
  private final AtomicInteger sqlInjectionAttempts;
  private final Timer logAnalysisTimer;

  public LogMetricsService(MeterRegistry meterRegistry) {
    this.totalLogsAnalyzed = meterRegistry.gauge("log_analysis_total", new AtomicInteger(0));
    this.bruteForceAttempts =
        meterRegistry.gauge("log_suspicious_bruteforce", new AtomicInteger(0));
    this.sqlInjectionAttempts =
        meterRegistry.gauge("log_suspicious_sqlinjection", new AtomicInteger(0));
    this.logAnalysisTimer = meterRegistry.timer("log_analysis_duration_seconds");
  }

  public void incrementAnalyzedLogs() {
    totalLogsAnalyzed.incrementAndGet();
  }

  public void incrementBruteForceAttempts() {
    bruteForceAttempts.incrementAndGet();
  }

  public void incrementSQLInjectionAttempts() {
    sqlInjectionAttempts.incrementAndGet();
  }

  public Timer.Sample startTimer() {
    return Timer.start();
  }

  public void stopTimer(Timer.Sample sample) {
    sample.stop(logAnalysisTimer);
  }
}
