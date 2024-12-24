package com.vishvendra.procart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ProfileDetailsDTO extends AbstractDTO {

  private String firstName;
  private String middleName;
  private String lastName;
  private String email;
  private String phone;
  private String whatsApp;
  private String address;
  private String city;
  private String state;
  private String country;
  private String pinCode;

}
