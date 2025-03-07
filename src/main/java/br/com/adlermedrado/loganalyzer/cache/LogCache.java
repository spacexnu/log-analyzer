package br.com.adlermedrado.loganalyzer.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class LogCache {
  private final Map<String, Long> cache = new ConcurrentHashMap<>();
  private final long expirationTimeMillis = TimeUnit.MINUTES.toMillis(5); // Cache expira em 5 min

  public boolean isDuplicate(String logMessage) {
    long now = System.currentTimeMillis();
    cache.entrySet().removeIf(entry -> now - entry.getValue() > expirationTimeMillis);

    if (cache.containsKey(logMessage)) {
      return true;
    }
    cache.put(logMessage, now);
    return false;
  }
}
