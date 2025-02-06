package com.vishvendra.procart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_configuration")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Configuration extends AbstractEntity {

  @Column(name = "key", nullable = false, unique = true)
  private String key;
  @Column(name = "value", nullable = false)
  private String value;
}
