package com.vishvendra.procart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Audit extends AbstractEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "action", nullable = false)
  private AuditAction action;
  @Column(name = "performed_by", nullable = false)
  private Long performedBy;
  @Column(name = "description")
  private String actionDescription;
}
