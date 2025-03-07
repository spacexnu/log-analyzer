package br.com.adlermedrado.loganalyzer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging {

  private final Logger logger;

  public Logging(Class<?> clazz) {
    this.logger = LoggerFactory.getLogger(clazz);
  }

  public static Logging getLogger(Class<?> clazz) {
    return new Logging(clazz);
  }

  public void info(String message, Object... args) {
    logger.info(message, args);
  }

  public void warn(String message, Object... args) {
    logger.warn(message, args);
  }

  public void error(String message, Object... args) {
    logger.error(message, args);
  }

  public void debug(String message, Object... args) {
    logger.debug(message, args);
  }
}
