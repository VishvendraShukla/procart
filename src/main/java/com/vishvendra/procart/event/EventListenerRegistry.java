package com.vishvendra.procart.event;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class EventListenerRegistry {

  private final Map<Class<? extends Event>, EventHandler<? extends Event>> registry = new HashMap<>();

  public <T extends Event> void registerHandler(Class<T> eventType, EventHandler<T> handler) {
    registry.put(eventType, handler);
  }

  @SuppressWarnings("unchecked")
  public <T extends Event> EventHandler<T> getHandler(Class<T> eventType) {
    return (EventHandler<T>) registry.get(eventType);
  }

}
