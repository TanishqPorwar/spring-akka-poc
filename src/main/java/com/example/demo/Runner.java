package com.example.demo;


import akka.actor.ActorSystem;
import com.example.demo.broker.producer.MessageProducer;
import com.example.demo.extension.akka.SpringAkkaExtension;
import com.example.demo.model.Task;
import com.example.demo.service.ActorSupervisorService;
import java.util.concurrent.TimeoutException;
import org.springframework.boot.CommandLineRunner;


class Runner implements CommandLineRunner {

  private final ActorSupervisorService actorSupervisorService;
  private final MessageProducer messageProducer;

  Runner(ActorSystem actorSystem, SpringAkkaExtension akkaExtension,
      ActorSupervisorService actorSupervisorService, MessageProducer messageProducer) {
    this.actorSupervisorService = actorSupervisorService;
    this.messageProducer = messageProducer;
  }

  @Override
  public void run(String[] args) throws Exception {
//    actorSimulation();
    kafkaSimulation();
  }

  private void kafkaSimulation() {
    for (int i = 0; i < 100; i++) {
      messageProducer.sendMessage(new Task("Message:" + i, String.valueOf(i)));
    }
  }

  private void actorSimulation() throws TimeoutException, InterruptedException {

    actorSupervisorService.submit(new Task("Hello", "World"));

  }
}
