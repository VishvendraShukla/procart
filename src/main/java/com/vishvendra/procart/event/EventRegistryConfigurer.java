package com.vishvendra.procart.event;

import com.vishvendra.procart.event.handler.AuditEventHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventRegistryConfigurer {

  private final EventListenerRegistry registry;
  private final AuditEventHandler auditEventHandler;


  @PostConstruct
  public void configure() {
    registry.registerHandler(AuditEvent.class, auditEventHandler);
  }
}

