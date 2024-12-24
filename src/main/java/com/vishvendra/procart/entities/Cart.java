package com.vishvendra.procart.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;

@Entity
@Table(name = "p_cart")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends AbstractEntity {

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToMany
  @JoinTable(
      name = "cart_product",
      joinColumns = @JoinColumn(name = "cart_id"),
      inverseJoinColumns = @JoinColumn(name = "product_id")
  )
  private List<Product> products;

  @ElementCollection
  @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"))
  private List<Item> items;

  @Enumerated(EnumType.STRING)
  @Column(name = "cart_status", nullable = false)
  private CartStatus cartStatus;

}
