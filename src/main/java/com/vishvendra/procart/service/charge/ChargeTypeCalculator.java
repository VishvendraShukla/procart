package com.vishvendra.procart.service.charge;

import com.vishvendra.procart.entities.ChargeType;
import java.math.BigDecimal;

public class ChargeTypeCalculator {

  private final ChargeType chargeType;

  public ChargeTypeCalculator(ChargeType chargeType) {
    this.chargeType = chargeType;
  }

  public BigDecimal calculateCharge(BigDecimal baseAmount, BigDecimal calculatedCharge) {
    return chargeType.equals(ChargeType.DISCOUNT)
        ? calculateForDiscount(baseAmount, calculatedCharge)
        : calculateForElse(baseAmount, calculatedCharge);

  }

  private BigDecimal calculateForDiscount(BigDecimal baseAmount, BigDecimal calculatedCharge) {
    return baseAmount.subtract(calculatedCharge);
  }

  private BigDecimal calculateForElse(BigDecimal baseAmount, BigDecimal calculatedCharge) {
    return baseAmount.add(calculatedCharge);
  }
}
