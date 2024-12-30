package com.vishvendra.procart.event.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishvendra.procart.entities.Audit;
import com.vishvendra.procart.event.AuditEvent;
import com.vishvendra.procart.event.EventHandler;
import com.vishvendra.procart.service.audit.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditEventHandler implements EventHandler<AuditEvent> {

  private final ObjectMapper objectMapper;
  private final AuditService auditService;

  @Override
  public void handleEvent(AuditEvent event) {
    try {
      auditService.createAudit(
          new Audit(event.getAuditAction(), event.getPerformedBy(), event.getMessage()));
    } catch (Exception e) {
      try {
        log.error("Error persisting AuditEvent: {}", objectMapper.writeValueAsString(event));
      } catch (JsonProcessingException ex) {
        log.error("Error parsing AuditEvent: {}", e.getMessage());
      }
    }
  }
}
