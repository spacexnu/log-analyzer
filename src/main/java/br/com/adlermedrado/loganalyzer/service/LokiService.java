package br.com.adlermedrado.loganalyzer.service;

import br.com.adlermedrado.loganalyzer.client.loki.LokiClient;
import br.com.adlermedrado.loganalyzer.client.loki.LokiQueryException;
import br.com.adlermedrado.loganalyzer.client.loki.LokiQueryResponse;
import org.springframework.stereotype.Service;

@Service
public class LokiService {
  private final LokiClient lokiClient;

  public LokiService(LokiClient lokiClient) {
    this.lokiClient = lokiClient;
  }

  public LokiQueryResponse queryLogs(String query, int limit, long start, long end)
      throws LokiQueryException {
    try {
      return lokiClient.queryLogs(query, limit, start, end);
    } catch (Exception e) {
      throw new LokiQueryException("Failed to fetch logs from Loki", e);
    }
  }
}
