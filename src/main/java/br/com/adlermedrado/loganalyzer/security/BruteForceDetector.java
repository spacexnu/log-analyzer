package br.com.adlermedrado.loganalyzer.security;

import br.com.adlermedrado.loganalyzer.domain.metrics.MetricName;
import br.com.adlermedrado.loganalyzer.util.Logging;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BruteForceDetector {
  private static final Logging log = new Logging(BruteForceDetector.class);
  private static final String IP_ADDRESS_PATTERN = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b";
  private static final String[] FAILED_LOGIN_INDICATORS = {"Failed login", "Invalid credentials"};
  private static final String BRUTE_FORCE_WARNING_MESSAGE = "Possible brute-force attack detected from IP: %s";

  private final Map<String, LoginAttemptTracker> failedLogins = new ConcurrentHashMap<>();
  private final Counter bruteForceCounter;

  @Value("${brute.force.max.attempts:5}")
  private int maxAttempts;

  @Value("${brute.force.time.window:60}")
  private int timeWindowSeconds;

  public BruteForceDetector(MeterRegistry meterRegistry) {
    bruteForceCounter = Counter.builder(MetricName.BRUTE_FORCE_DETECTED.getMetricName())
      .description("Counts detected brute force attempts")
      .register(meterRegistry);
  }

  public void analyzeLog(String logMessage) {
    extractIpFromLog(logMessage)
      .filter(ip -> isLoginFailureAttempt(logMessage))
      .ifPresent(this::recordFailedAttempt);
  }

  private void recordFailedAttempt(String ip) {
    LoginAttemptTracker tracker = failedLogins.computeIfAbsent(ip,
      k -> new LoginAttemptTracker(timeWindowSeconds));
    tracker.addAttempt();

    if (tracker.getRecentAttemptsCount() >= maxAttempts) {
      bruteForceCounter.increment();
      log.warn(String.format(BRUTE_FORCE_WARNING_MESSAGE, ip));
    }
  }

  private boolean isLoginFailureAttempt(String logMessage) {
    return List.of(FAILED_LOGIN_INDICATORS).stream()
      .anyMatch(logMessage::contains);
  }

  private Optional<String> extractIpFromLog(String logMessage) {
    return List.of(logMessage.split("\\s+")).stream()
      .filter(this::isValidIpAddress)
      .findFirst();
  }

  private boolean isValidIpAddress(String word) {
    return word.matches(IP_ADDRESS_PATTERN);
  }

  private static class LoginAttemptTracker {
    private final List<Instant> attempts = new java.util.ArrayList<>();
    private final int timeWindowSeconds;

    LoginAttemptTracker(int timeWindowSeconds) {
      this.timeWindowSeconds = timeWindowSeconds;
    }

    void addAttempt() {
      removeExpiredAttempts();
      attempts.add(Instant.now());
    }

    int getRecentAttemptsCount() {
      removeExpiredAttempts();
      return attempts.size();
    }

    private void removeExpiredAttempts() {
      Instant cutoffTime = Instant.now().minusSeconds(timeWindowSeconds);
      attempts.removeIf(attempt -> attempt.isBefore(cutoffTime));
    }
  }
}