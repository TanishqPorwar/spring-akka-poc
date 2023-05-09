package com.example.demo.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import com.example.demo.actor.BaseSupervisor;
import com.example.demo.extension.akka.SpringAkkaExtension;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import scala.concurrent.Future;

/**
 * Supervisor service. This service is used to submit messages to the supervisor actor.
 * <p>
 * BaseSupervisor is an actor that is responsible for creating and supervising other actors.
 * BaseSupervisor bean has to be prototype, as all actors are managed by akka
 * </p>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ActorSupervisorService {

  private final ActorSystem actorSystem;
  private final SpringAkkaExtension akkaExtension;
  private ActorRef supervisor;

  public Future<Object> ask(Object eventMessage) {
    return Patterns.ask(supervisor, eventMessage, 1000);
  }

  @PostConstruct
  public void postConstruct() {
    supervisor = actorSystem.actorOf(akkaExtension.get(actorSystem).props(BaseSupervisor.class),
        "supervisor");
  }

  public void submit(Object message) {
    log.info("Submitting message to supervisor: {}", message);
    supervisor.tell(message, ActorRef.noSender());
  }

}
