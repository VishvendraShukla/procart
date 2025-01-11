package com.vishvendra.procart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
public class ChargeDTO extends AbstractDTO {

  @NotNull
  private String chargeType;
  @NotNull
  private String chargeAmountType;
  @NotNull
  private String chargeAppliesOn;
  @NotNull
  private String amount;
  private String description;
  private String displayName;

}
