package com.vishvendra.procart.utils.payment.stripe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StripePaymentLinkRequest {

  private String currencyCode;
  private Long amount;
  private String orderId;
  private String traceId;
}
