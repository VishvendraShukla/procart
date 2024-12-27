package com.vishvendra.procart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class AdminDTO extends AbstractDTO {

  @NotNull
  @NotBlank
  private String name;
  @NotNull
  @NotBlank
  private String password;
  @NotNull
  @NotBlank
  private String username;
  private ProfileDetailsDTO profileDetailsDTO;

}
