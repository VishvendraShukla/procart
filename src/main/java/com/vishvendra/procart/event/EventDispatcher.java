package com.vishvendra.procart.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EventDispatcher {

  private final EventListenerRegistry registry;

  public EventDispatcher(EventListenerRegistry registry) {
    this.registry = registry;
  }

  @SuppressWarnings("unchecked")
  @Async("eventTaskExecutor")
  public <T extends Event> void dispatchEvent(T event) {
    EventHandler<T> handler = (EventHandler<T>) registry.getHandler(event.getClass());
    if (handler != null) {
      handler.handleEvent(event);
    } else {
      throw new IllegalArgumentException("No handler found for event type: " + event.getClass());
    }
  }
}