package com.vishvendra.procart.utils.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLinkResponse<R> {

  private String paymentLink;
  private R otherData;
}
