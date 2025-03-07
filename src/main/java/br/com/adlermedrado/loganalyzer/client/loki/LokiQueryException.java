package br.com.adlermedrado.loganalyzer.client.loki;

public class LokiQueryException extends Exception {
  public LokiQueryException(String message, Throwable cause) {
    super(message, cause);
  }
}
