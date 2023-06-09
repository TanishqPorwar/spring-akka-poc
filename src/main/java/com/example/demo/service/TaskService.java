package com.example.demo.service;

import com.example.demo.model.Task;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskService {

  private final Random random = new Random();

  public String process(Task task) {
    try {
      Thread.sleep(random.nextInt(100, 500));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return task.message() + task.name();
  }

}
