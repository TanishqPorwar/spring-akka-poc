package com.example.demo.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class AppConfiguration {

  private final ApplicationContext applicationContext;

  public AppConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
