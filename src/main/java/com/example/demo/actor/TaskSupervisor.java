package com.example.demo.actor;

import akka.actor.ActorSystem;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.example.demo.annotations.Actor;
import com.example.demo.extension.akka.SpringAkkaExtension;
import com.example.demo.model.Task;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Actor
public class TaskSupervisor extends AbstractBaseActor {

  private static final AtomicInteger counter = new AtomicInteger(0);

  private Router router;

  public TaskSupervisor(SpringAkkaExtension akkaExtension, ActorSystem actorSystem) {
    super(akkaExtension, actorSystem);
    id = "TaskSupervisor" + counter.incrementAndGet();
  }

  @Override
  public void preStart() throws Exception {
    super.preStart();
    log().info("TaskSupervisor started");
    List<Routee> routees = createChildRouteActorsOfType(TaskWorker.class, i -> "taskWorker" + i, 5);
    router = new Router(new RoundRobinRoutingLogic(), routees);
  }

  @Override
  public void postStop() throws Exception {
    super.postStop();
    log().info("TaskSupervisor stopped");
  }


  @Override
  public Receive createReceive() {
    return receiveBuilder().match(Task.class, message -> {
      router.route(message, getSender());
    }).matchAny(o -> log().info("Received unknown message: {}", o)).build();
  }
}
