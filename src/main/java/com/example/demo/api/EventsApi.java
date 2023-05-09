package com.example.demo.api;

import com.example.demo.broker.producer.MessageProducer;
import com.example.demo.model.EventMessage;
import com.example.demo.model.Task;
import com.example.demo.service.ActorSupervisorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scala.concurrent.Future;
import scala.jdk.javaapi.FutureConverters;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EventsApi {

  private final ActorSupervisorService actorSupervisorService;
  private final MessageProducer messageProducer;

  /**
   * Submit event message.
   *
   * @param eventMessage EventMessage
   */
  @PostMapping("/event/submit")
  public void submit(@RequestBody EventMessage eventMessage) {
    actorSupervisorService.submit(eventMessage);
  }

  /**
   * Submit event message and return response.
   *
   * @param eventMessage EventMessage
   * @return Object
   */
  @PostMapping("/event/ask")
  public Object ask(@RequestBody EventMessage eventMessage) {
    Future<Object> obj = actorSupervisorService.ask(eventMessage);
    return FutureConverters.asJava(obj).toCompletableFuture().join();

  }

  /**
   * Bulk submit task to queue.
   */
  @PostMapping("task/bulk/submit")
  public void submitBulk() {
    for (int i = 0; i < 100; i++) {
      messageProducer.sendMessage(new Task("Message:" + i, String.valueOf(i)));
    }
  }

}
