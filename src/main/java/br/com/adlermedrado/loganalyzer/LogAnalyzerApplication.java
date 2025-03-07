package br.com.adlermedrado.loganalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class LogAnalyzerApplication {
  public static void main(String[] args) {
    SpringApplication.run(LogAnalyzerApplication.class, args);
  }
}
