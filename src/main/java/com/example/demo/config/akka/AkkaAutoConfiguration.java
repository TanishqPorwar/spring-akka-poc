package com.example.demo.config.akka;

import akka.actor.ActorSystem;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AkkaProperties.class)
public class AkkaAutoConfiguration {

  /**
   * Creates an actor system. If the system name and config are not provided, then the default
   * actor system is created.
   * @param akkaProperties configuration properties for akka
   * @return the actor system
   */
  @Bean(destroyMethod = "terminate")
  @ConditionalOnMissingBean
  public ActorSystem getActorSystem(AkkaProperties akkaProperties) {
    ActorSystem system;
    if (akkaProperties.getSystemName() != null && akkaProperties.getConfig() != null) {
      system = ActorSystem.create(akkaProperties.getSystemName(), akkaProperties.getConfig());
    } else if (akkaProperties.getSystemName() != null) {
      system = ActorSystem.create(akkaProperties.getSystemName());
    } else {
      system = ActorSystem.create();
    }
    return system;
  }

}