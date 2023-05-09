package com.example.demo.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.example.demo.annotations.Actor;
import com.example.demo.extension.akka.SpringAkkaExtension;
import com.example.demo.model.EventMessage;
import com.example.demo.model.Task;
import java.util.concurrent.atomic.AtomicInteger;

@Actor
public class BaseSupervisor extends AbstractBaseActor {

  private static final AtomicInteger counter = new AtomicInteger(0);

  private final ActorRef taskSupervisor;
  private final ActorRef eventSupervisor;

  public BaseSupervisor(SpringAkkaExtension akkaExtension, ActorSystem actorSystem) {
    super(akkaExtension, actorSystem);
    taskSupervisor = actorSystem.actorOf(akkaExtension.get(actorSystem).props(TaskSupervisor.class),
        "taskSupervisor");
    eventSupervisor = actorSystem.actorOf(
        akkaExtension.get(actorSystem).props(EventSupervisor.class), "eventSupervisor");
    id = "BaseSupervisor" + counter.incrementAndGet();

  }

  @Override
  public Receive createReceive() {
    return receiveBuilder().match(Task.class, message -> {
      taskSupervisor.tell(message, getSender());
    }).match(EventMessage.class, message -> {
      eventSupervisor.tell(message, getSender());
    }).matchAny(o -> log().info("Received unknown message: {}", o)).build();
  }
}
