package com.example.demo.service;

import com.example.demo.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskService {

  public String process(Message message) {
    log.info("Processing message");
    // sleep for 5 seconds
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    log.info("{} : {}", message.getMessage(), message.getName());
    return message.getMessage() + message.getName();
  }

}
