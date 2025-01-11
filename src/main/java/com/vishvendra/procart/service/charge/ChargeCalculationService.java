package com.vishvendra.procart.service.charge;

import com.vishvendra.procart.entities.Charge;
import com.vishvendra.procart.entities.Product;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class ChargeCalculationService {

  public BigDecimal calculate(Product product) {
    BigDecimal productPrice = product.getPrice();
    for (Charge charge : product.getCharges()) {
      BigDecimal calculatedCharge = new ChargeAmountTypeCalculator(
          charge.getChargeAmountType()).calculateCharge(productPrice, charge.getAmount());
      productPrice = new ChargeTypeCalculator(charge.getChargeType())
          .calculateCharge(productPrice, calculatedCharge);
    }
    return productPrice.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
  }


}
