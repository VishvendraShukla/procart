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
@Table(name = "p_address")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address extends AbstractEntity {

  @Column(name = "address_line_1")
  private String addressLine1;
  @Column(name = "address_line_2")
  private String addressLine2;
  @Column(name = "city", length = 100)
  private String city;
  @Column(name = "state", length = 50)
  private String state;
  @Column(name = "country", length = 50)
  private String country;
  @Column(name = "pin_code", length = 10)
  private String pinCode;
}
