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
@Table(name = "p_product_currency")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCurrency extends AbstractEntity {

  @Column(name = "code", unique = true)
  private String code;

  @Column(name = "symbol", unique = true)
  private String symbol;

  @Column(name = "name")
  private String name;

  @Column(name = "decimalPrecision", precision = 6)
  private Double decimalPrecision;
}
