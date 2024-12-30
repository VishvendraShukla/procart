package com.vishvendra.procart.event;

import com.vishvendra.procart.entities.AuditAction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditEvent extends Event {

  private final String message;
  private final String performedBy;
  private final AuditAction auditAction;

  public AuditEvent(String message, String performedBy, AuditAction auditAction) {
    super("AuditEvent");
    this.message = message;
    this.performedBy = performedBy;
    this.auditAction = auditAction;
  }
}
