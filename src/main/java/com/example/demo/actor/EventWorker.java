package com.example.demo.actor;

import akka.actor.ActorSystem;
import com.example.demo.annotations.Actor;
import com.example.demo.extension.akka.SpringAkkaExtension;
import com.example.demo.model.EventMessage;
import com.example.demo.service.EventService;
import java.util.concurrent.atomic.AtomicInteger;

@Actor
public class EventWorker extends AbstractBaseActor {

  private final AtomicInteger counter = new AtomicInteger(0);
  private final EventService eventService;

  public EventWorker(SpringAkkaExtension akkaExtension, ActorSystem actorSystem,
      EventService eventService) {
    super(akkaExtension, actorSystem);
    this.eventService = eventService;
    id = "EventWorker" + counter.incrementAndGet();
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder().match(EventMessage.class, message -> {
      log().info("Received message: {} by actor: {}, processing {}th task", message.eventId(),
          self().path().name(), counter.incrementAndGet());
      eventService.process(message);
      sender().tell(message, self());
    }).matchAny(o -> log().info("Received unknown message: {}", o)).build();
  }
}
