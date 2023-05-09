package com.example.demo.actor;

import akka.actor.ActorSystem;
import akka.routing.Routee;
import akka.routing.Router;
import com.example.demo.actor.router.CustomRoutingLogic;
import com.example.demo.annotations.Actor;
import com.example.demo.extension.akka.SpringAkkaExtension;
import com.example.demo.model.EventMessage;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Actor
public class EventSupervisor extends AbstractBaseActor {

  private static final AtomicInteger counter = new AtomicInteger(0);

  private Router router;

  public EventSupervisor(SpringAkkaExtension akkaExtension, ActorSystem actorSystem) {
    super(akkaExtension, actorSystem);
  }

  @Override
  public void preStart() throws Exception {
    super.preStart();
    List<Routee> routees = createChildRouteActorsOfType(EventWorker.class, i -> "eventWorker" + i,
        5);
    router = new Router(new CustomRoutingLogic(), routees);
    id = "EventSupervisor" + counter.incrementAndGet();
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder().match(EventMessage.class, message -> {
      router.route(message, getSender());
    }).matchAny(o -> log().info("Received unknown message: {}", o)).build();
  }
}
