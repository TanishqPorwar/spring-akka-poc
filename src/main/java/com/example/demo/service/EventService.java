package com.example.demo.service;

import com.example.demo.model.EventMessage;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventService {

  private final Random random = new Random();

  public void process(EventMessage message) {
    try {
      Thread.sleep(random.nextInt(100, 500));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    log.info("Event received message: {}", message);
  }

}
