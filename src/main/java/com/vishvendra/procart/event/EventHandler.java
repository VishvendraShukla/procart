package com.vishvendra.procart.event;

public interface EventHandler<T extends Event> {
  // TODO: Use this for future notification events, till then only using for audit events
  void handleEvent(T event);

}
