package com.example.demo.actor;

import akka.actor.AbstractLoggingActor;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import akka.routing.ActorRefRoutee;
import akka.routing.Routee;
import com.example.demo.extension.akka.SpringAkkaExtension;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractBaseActor extends AbstractLoggingActor {

  @Value("${akka.config.supervisor.restart.max_retries:10}")
  private static int MAX_RETRIES;

  // Supervisor strategy: restart the child actor in case of any failure
  private static final SupervisorStrategy RESTART_STRATEGY = new OneForOneStrategy(MAX_RETRIES,
      Duration.ofMinutes(1), DeciderBuilder.matchAny(e -> SupervisorStrategy.restart()).build());

  // Spring extension to create the actor bean
  private final SpringAkkaExtension akkaExtension;

  private final ActorSystem actorSystem;

  protected String id;
  protected String parentId;

  protected AbstractBaseActor(SpringAkkaExtension akkaExtension, ActorSystem actorSystem) {
    this.akkaExtension = akkaExtension;
    this.actorSystem = actorSystem;
  }

  @Override
  public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
    super.preRestart(reason, message);
    log().warning("[{}#{}] Actor restarting due to {}", parentId, id, reason);
  }

  /**
   * A helper method to create child actors of the given type.
   *
   * @param actorClz          The type of actor to be created
   * @param actorNameSupplier A function to generate the name of the actor
   * @param count             The number of actors to be created
   * @return A list of {@link Routee} objects
   */
  protected <T extends Actor> List<Routee> createChildRouteActorsOfType(Class<T> actorClz,
      Function<Integer, String> actorNameSupplier, int count) {
    log().info("[{}] Creating {} actors of type {}", id, count, actorClz.getSimpleName());
    List<Routee> allRoutees = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      ActorRef actor = actorSystem.actorOf(akkaExtension.get(actorSystem).props(actorClz),
          actorNameSupplier.apply(i));
      getContext().watch(actor);
      allRoutees.add(new ActorRefRoutee(actor));
    }
    return allRoutees;
  }

  @Override
  public SupervisorStrategy supervisorStrategy() {
    return RESTART_STRATEGY;
  }

}
