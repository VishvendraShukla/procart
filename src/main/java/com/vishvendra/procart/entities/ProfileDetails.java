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
@Table(name = "p_profile_details")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDetails extends AbstractEntity {

  @Column(name = "first_name", length = 50)
  private String firstName;
  @Column(name = "middle_name", length = 50)
  private String middleName;
  @Column(name = "last_name", length = 50)
  private String lastName;
  @Column(name = "email", length = 30)
  private String email;
  @Column(name = "phone", length = 15)
  private String phone;
  @Column(name = "whats_app", length = 15)
  private String whatsApp;
  @Column(name = "address", length = 100)
  private String address;
  @Column(name = "city", length = 10)
  private String city;
  @Column(name = "state", length = 50)
  private String state;
  @Column(name = "country", length = 50)
  private String country;
  @Column(name = "pin_code", length = 10)
  private String pinCode;

}
