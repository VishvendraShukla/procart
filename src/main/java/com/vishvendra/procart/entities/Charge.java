package com.vishvendra.procart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_charges")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Charge extends AbstractEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "charge_type", nullable = false)
  private ChargeType chargeType;

  @Enumerated(EnumType.STRING)
  @Column(name = "charge_amount_type", nullable = false)
  private ChargeAmountType chargeAmountType;

  @Enumerated(EnumType.STRING)
  @Column(name = "charge_applies_on", nullable = false)
  private ChargeAppliesOn chargeAppliesOn;

  @Column(name = "charge_amount", nullable = false)
  private BigDecimal amount;

  @Column(name = "charge_description")
  private String description;

  @Column(name = "charge_display_name")
  private String displayName;

}
