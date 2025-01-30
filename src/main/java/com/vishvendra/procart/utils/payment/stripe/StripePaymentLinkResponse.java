package com.vishvendra.procart.utils.payment.stripe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StripePaymentLinkResponse {

  private String paymentLinkReferenceId;
}
