package com.example.demo.broker.consumer;

import com.example.demo.model.Task;
import com.example.demo.service.ActorSupervisorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListenerManager {

  private final ActorSupervisorService actorSupervisorService;
  @Value("${topic.name.consumer}")
  private String topicName;

  @KafkaListener(topics = "${topic.name.consumer}", containerFactory = "kafkaListenerContainerFactory")
  public void listenMessage(ConsumerRecord<String, Task> consumerRecord) {
    log.info("Received message from topic: {} with message: {}", topicName, consumerRecord.value());
    actorSupervisorService.submit(consumerRecord.value());
  }


}
