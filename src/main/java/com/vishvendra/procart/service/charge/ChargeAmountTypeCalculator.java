package com.vishvendra.procart.service.charge;

import com.vishvendra.procart.entities.ChargeAmountType;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ChargeAmountTypeCalculator {

  private final ChargeAmountType chargeAmountType;

  public ChargeAmountTypeCalculator(ChargeAmountType chargeAmountType) {
    this.chargeAmountType = chargeAmountType;
  }

  public BigDecimal calculateCharge(BigDecimal baseAmount, BigDecimal chargeAmount) {
    return chargeAmountType.equals(ChargeAmountType.PERCENTAGE)
        ? baseAmount.multiply(chargeAmount.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP))
        : chargeAmount;
  }

}
