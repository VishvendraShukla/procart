package com.vishvendra.procart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_transactions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends AbstractEntity {

  @Column(name = "currency", nullable = false)
  private String currency;

  @Column(name = "total_amount", nullable = false)
  private BigDecimal totalAmount;

  @Column(name = "external_reference_id", unique = true)
  private String referenceId;

  @Column(name = "trace_id", unique = true)
  private String traceId;

  @Column(name = "external_reference_name")
  private String referenceName;

  @Column(name = "description", length = 200)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private TransactionStatus status;

  @OneToOne
  @JoinColumn(name = "cart_id")
  private Cart cart;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToMany
  @JoinTable(
      name = "transaction_charges",
      joinColumns = @JoinColumn(name = "transaction_id"),
      inverseJoinColumns = @JoinColumn(name = "charge_id")
  )
  private List<Charge> charges;
}
