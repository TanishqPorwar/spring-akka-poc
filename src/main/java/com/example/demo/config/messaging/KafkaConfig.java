package com.example.demo.config.messaging;

import com.example.demo.broker.consumer.TaskDeserializer;
import com.example.demo.model.Task;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
@EnableKafka
public class KafkaConfig {

  @Bean
  @Qualifier("defaultConsumerFactory")
  public ConsumerFactory<String, Task> consumerFactory() {
    Map<String, Object> config = new HashMap<>();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    config.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, TaskDeserializer.class);
    return new DefaultKafkaConsumerFactory<>(config);
  }

  @Bean
  @Qualifier("defaultKafkaListenerContainerFactory")
  public ConcurrentKafkaListenerContainerFactory<String, Task> kafkaListenerContainerFactory(
      @Qualifier("defaultConsumerFactory") ConsumerFactory<String, Task> consumerFactory) {
    ConcurrentKafkaListenerContainerFactory<String, Task> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.setConcurrency(3);
    return factory;
  }

}
