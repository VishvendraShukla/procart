package com.vishvendra.procart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_admin")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Admin extends AbstractEntity {

  @Column(name = "name")
  private String name;
  @Column(name = "password")
  private String password;
  @Column(name = "username", unique = true)
  private String username;
  @OneToOne
  @JoinColumn(name = "profile_detail_id")
  private ProfileDetails profileDetails;
}
