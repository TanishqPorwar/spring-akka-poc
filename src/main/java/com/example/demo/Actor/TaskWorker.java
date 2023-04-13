package com.example.demo.Actor;

import akka.actor.AbstractLoggingActor;
import com.example.demo.model.Message;
import com.example.demo.service.TaskService;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskWorker extends AbstractLoggingActor {

  private final AtomicInteger counter = new AtomicInteger(0);

  private final TaskService taskService;

  public TaskWorker(TaskService taskService) {
    this.taskService = taskService;
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(Message.class, taskService::process)
        .matchAny(o -> log().info("Received unknown message: {}", o))
        .build();
  }
}
