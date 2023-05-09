package com.example.demo.actor.router;

import akka.routing.Routee;
import akka.routing.RoutingLogic;
import com.example.demo.model.EventMessage;
import scala.collection.immutable.IndexedSeq;

/**
 * Custom routing logic for routing messages to routees.
 * <p>
 *   This routing logic is used by {@link com.example.demo.actor.EventSupervisor} to route messages
 *   to {@link com.example.demo.actor.EventWorker} actors.
 *   <br>
 *  </p>
 */
public class CustomRoutingLogic implements RoutingLogic {

  @Override
  public Routee select(Object message, IndexedSeq<Routee> routees) {
    if (message instanceof EventMessage eventMessage) {
      return routees.apply(eventMessage.eventId().hashCode() % routees.size());
    }
    return null;
  }
}
