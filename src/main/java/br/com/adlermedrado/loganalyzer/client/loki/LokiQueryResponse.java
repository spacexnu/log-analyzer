package br.com.adlermedrado.loganalyzer.client.loki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LokiQueryResponse(
    @JsonProperty("status") String status, @JsonProperty("data") Data data) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Data(
      @JsonProperty("resultType") String resultType, @JsonProperty("result") List<Result> result) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Result(
      @JsonProperty("stream") Map<String, String> stream,
      @JsonProperty("values") List<List<String>> values) {}
}
