package com.vishvendra.procart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_product")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends AbstractEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "sku", unique = true, nullable = false)
  private String sku;

  @Column(name = "price")
  private BigDecimal price;

  @Column(name = "image", columnDefinition = "BYTEA", nullable = true)
  private byte[] image;

  @Column(name = "image_url")
  private String imageUrl;

  @ManyToOne
  @JoinColumn(name = "currency_id")
  private ProductCurrency currency;

  @Column(name = "quantity", nullable = false)
  private Long quantity;

}
