package com.example.demo.broker.producer;

import com.example.demo.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProducer {

  private final KafkaTemplate<String, Task> kafkaTemplate;
  @Value("${topic.name.producer}")
  private String topicName;

  public void sendMessage(Task task) {
    log.info("Sending message to topic: {} with message: {}", topicName, task);
    kafkaTemplate.send(topicName, task);
  }

}
