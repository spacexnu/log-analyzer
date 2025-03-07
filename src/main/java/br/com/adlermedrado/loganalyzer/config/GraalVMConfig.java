package com.example.loganalyzer.config;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraalVMConfig implements RuntimeHintsRegistrar {

  @Bean
  public static RuntimeHintsRegistrar runtimeHints() {
    return new GraalVMConfig();
  }

  @Override
  public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
    //    hints.reflection().registerType(com.example.loganalyzer.service.LogFetcherService.class);
    //    hints.reflection().registerType(com.example.loganalyzer.service.AnomalyDetection.class);
    //    hints.reflection().registerType(com.example.loganalyzer.utils.OllamaClient.class);
  }
}
