package br.com.adlermedrado.loganalyzer.client.loki;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "lokiClient", url = "${loki.url}")
public interface LokiClient {

  @GetMapping("/loki/api/v1/query_range")
  LokiQueryResponse queryLogs(
      @RequestParam("query") String query,
      @RequestParam("limit") int limit,
      @RequestParam("start") long start,
      @RequestParam("end") long end);
}
