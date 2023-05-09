package com.example.demo.config.akka;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties(prefix = "spring.akka")
public class AkkaProperties {

  private String systemName;
  private Config config;

  public void setConfig(String config) {
    Config defaultConfig = ConfigFactory.empty();
    this.config = defaultConfig.withFallback(ConfigFactory.load((config))).resolve();
  }
}