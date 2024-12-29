package com.vishvendra.procart.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_cart",
    indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_cart_status", columnList = "cart_status"),
        @Index(name = "idx_total_price", columnList = "total_price")
    })
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends AbstractEntity {

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ElementCollection
  @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"))
  @OrderColumn(name = "item_order")
  private List<Items> items;

  @Column(name = "total_price", nullable = false)
  private BigDecimal totalPrice;

  @Enumerated(EnumType.STRING)
  @Column(name = "cart_status", nullable = false)
  private CartStatus cartStatus;

}
