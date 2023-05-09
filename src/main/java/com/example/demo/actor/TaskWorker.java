package com.example.demo.actor;

import akka.actor.ActorSystem;
import com.example.demo.annotations.Actor;
import com.example.demo.extension.akka.SpringAkkaExtension;
import com.example.demo.model.Task;
import com.example.demo.service.TaskService;
import java.util.concurrent.atomic.AtomicInteger;

@Actor
public class TaskWorker extends AbstractBaseActor {

  private final AtomicInteger counter = new AtomicInteger(0);
  private final TaskService taskService;

  public TaskWorker(TaskService taskService, SpringAkkaExtension akkaExtension,
      ActorSystem actorSystem) {
    super(akkaExtension, actorSystem);
    this.taskService = taskService;
    id = "TaskWorker" + counter.incrementAndGet();
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder().match(Task.class, msg -> {
      log().info("Received message: {} by actor: {}, processing {}th task", msg.getName(),
          self().path().name(), counter.incrementAndGet());
      taskService.process(msg);
    }).matchAny(o -> log().info("Received unknown message: {}", o)).build();
  }

  @Override
  public void postStop() throws Exception {
    super.postStop();
    log().info("TaskWorker {} stopped. Processed {} messages", self().path().name(), counter.get());
  }
}
