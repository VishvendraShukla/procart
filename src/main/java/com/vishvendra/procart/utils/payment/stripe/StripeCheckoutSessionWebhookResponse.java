package com.vishvendra.procart.utils.payment.stripe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StripeCheckoutSessionWebhookResponse {

  private String payload;
  private String sigHeader;

}
