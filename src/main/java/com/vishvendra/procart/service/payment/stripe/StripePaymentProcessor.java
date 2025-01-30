package com.vishvendra.procart.service.payment.stripe;

import com.vishvendra.procart.service.payment.PaymentProcessor;
import org.springframework.stereotype.Service;

@Service("stripePaymentProcessor")
public class StripePaymentProcessor implements PaymentProcessor {

  @Override
  public String createPaymentLink() {
    return "";
  }
}
