package com.vishvendra.procart.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Items {

  @ManyToOne
  @JoinColumn(name = "product_id") // Foreign key to Product
  private Product product;

  private Long quantity;  // Quantity of the product in the cart

  private BigDecimal price;  // Price of the product at the time of adding to the cart
}

