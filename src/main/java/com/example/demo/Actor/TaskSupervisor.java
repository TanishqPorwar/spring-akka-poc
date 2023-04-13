package com.example.demo.Actor;

import static com.example.demo.config.SpringExtension.SPRING_EXTENSION_PROVIDER;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.example.demo.model.Message;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskSupervisor extends AbstractLoggingActor {


  private static final SupervisorStrategy RESTART_STRATEGY = new OneForOneStrategy(
      5,
      Duration.ofMinutes(1),
      DeciderBuilder.matchAny(e -> SupervisorStrategy.restart())
          .build()
  );
  private Router router;

  @Override
  public void preStart() throws Exception {
    super.preStart();
    log().info("TaskSupervisor started");
    List<Routee> routees = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      ActorRef actorRef = getContext().actorOf(SPRING_EXTENSION_PROVIDER.get(getContext().system())
          .props("taskWorker"), "taskWorker" + i);
      getContext().watch(actorRef);
      routees.add(new ActorRefRoutee(actorRef));
    }
    router = new Router(new RoundRobinRoutingLogic(), routees);
  }

  @Override
  public void postStop() throws Exception {
    super.postStop();
    log().info("TaskSupervisor stopped");
  }

  @Override
  public SupervisorStrategy supervisorStrategy() {
    return RESTART_STRATEGY;
  }


  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(Message.class, message -> {
          router.route(message, getSender());
        })
        .matchAny(o -> log().info("Received unknown message: {}", o)).build();
  }
}
