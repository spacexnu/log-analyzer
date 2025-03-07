package br.com.adlermedrado.loganalyzer.scheduler;

import br.com.adlermedrado.loganalyzer.service.LogAnalysisService;
import br.com.adlermedrado.loganalyzer.util.Logging;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LogAnalysisScheduler {

  private static final Logging log = new Logging(LogAnalysisScheduler.class);
  private final LogAnalysisService logAnalysisService;

  public LogAnalysisScheduler(LogAnalysisService logAnalysisService) {
    this.logAnalysisService = logAnalysisService;
  }

  @Scheduled(fixedRateString = "${LOG_ANALYSIS_INTERVAL:60000}")
  public void scheduleLogAnalysis() {
    log.info("Running scheduled log analysis...");
    logAnalysisService.analyzeLogs();
  }
}
