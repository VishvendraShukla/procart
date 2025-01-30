package com.vishvendra.procart.service.charge;

import com.vishvendra.procart.entities.Cart;
import com.vishvendra.procart.entities.Charge;
import com.vishvendra.procart.entities.Product;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
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

  private BigDecimal calculate(Charge charge, BigDecimal baseAmount) {
    BigDecimal calculatedCharge = new ChargeAmountTypeCalculator(
        charge.getChargeAmountType()).calculateCharge(baseAmount, charge.getAmount());
    return new ChargeTypeCalculator(charge.getChargeType())
        .calculateCharge(baseAmount, calculatedCharge);
  }

  public BigDecimal calculateTotalAmount(Cart cart, List<Charge> charges) {
    BigDecimal productPricesAfterDiscount = calculateTotalPrice(cart);
    BigDecimal finalAmount = productPricesAfterDiscount;
    if (Objects.nonNull(charges)) {
      finalAmount = charges.stream().map(charge ->
          calculate(charge, productPricesAfterDiscount)
      ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    return finalAmount;
  }

  public BigDecimal calculateTotalPrice(Cart cart) {
    return cart.getItems().stream()
        .map(item -> calculate(item.getProduct())
            .multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
