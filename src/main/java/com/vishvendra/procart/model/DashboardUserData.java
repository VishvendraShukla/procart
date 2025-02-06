package com.vishvendra.procart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DashboardUserData {

  private String username;
  private String email;
  private String phone;
  private String whatsApp;
  private Boolean hasAddress;
  private String address;
}
