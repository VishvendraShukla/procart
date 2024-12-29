package com.vishvendra.procart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_inventory",
    indexes = {
        @Index(name = "idx_product_id", columnList = "product_id")
    })
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inventory extends AbstractEntity {

  @OneToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(name = "totalStock")
  private Long totalStock;

  @Column(name = "lockedStock")
  private Long lockedStock;

  @Column(name = "availableStock")
  private Long availableStock;

  @Version
  @Column(name = "version")
  private Long version;

}
