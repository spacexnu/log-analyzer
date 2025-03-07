package br.com.adlermedrado.loganalyzer.domain.metrics;

public enum MetricName {
  BRUTE_FORCE_DETECTED("log_analyzer.brute_force_detected"),
  SQL_INJECTION_DETECTED("log_analyzer.sql_injection_detected"),
  XSS_DETECTED("log_analyzer.xss_detected"),
  RCE_DETECTED("log_analyzer.rce_detected"),
  PATH_TRAVERSAL_DETECTED("log_analyzer.path_traversal_detected");

  private final String name;

  MetricName(String name) {
    this.name = name;
  }

  public String getMetricName() {
    return name;
  }
}
