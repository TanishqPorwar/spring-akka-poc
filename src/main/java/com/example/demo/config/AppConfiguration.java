package com.example.demo.config;

import static com.example.demo.config.SpringExtension.SPRING_EXTENSION_PROVIDER;

import akka.actor.ActorSystem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class AppConfiguration {
  private final ApplicationContext applicationContext;

  public AppConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Bean
  public ActorSystem actorSystem() {
    ActorSystem system = ActorSystem.create("akka-spring-demo");
    SPRING_EXTENSION_PROVIDER.get(system)
        .initialize(applicationContext);
    return system;
  }
}
