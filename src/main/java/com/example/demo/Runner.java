package com.example.demo;

import static com.example.demo.config.SpringExtension.SPRING_EXTENSION_PROVIDER;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.example.demo.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

@Component
class Runner implements CommandLineRunner {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final ActorSystem actorSystem;

  Runner(ActorSystem actorSystem) {
    this.actorSystem = actorSystem;
  }

  @Override
  public void run(String[] args) throws Exception {
    try {
      ActorRef taskSupervisor = actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem).props("taskSupervisor"), "taskSupervisor");

      taskSupervisor.tell(new Message("Hello", "World"), null);
//      workerActor.tell(new WorkerActor.Request(), null);
//      workerActor.tell(new WorkerActor.Request(), null);

//      FiniteDuration duration = FiniteDuration.create(1, TimeUnit.SECONDS);
//      Future<Object> awaitable = Patterns.ask(taskSupervisor, new WorkerActor.Response(), Timeout.durationToTimeout(duration));

//      logger.info("Response: " + Await.result(awaitable, duration));
    } finally {
      actorSystem.terminate();
      Await.result(actorSystem.whenTerminated(), Duration.Inf());
    }
  }
}
