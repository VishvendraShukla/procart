package com.vishvendra.procart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

  @NotNull
  @NotBlank
  private String firstName;
  private String middleName;
  @NotNull
  @NotBlank
  private String lastName;
  @Email
  private String email;
  @Size(min = 10, max = 15)
  private String phone;
  @Size(min = 10, max = 15)
  private String whatsApp;
  private String address;
  private String city;
  private String state;
  private String country;
  private String pinCode;

}
