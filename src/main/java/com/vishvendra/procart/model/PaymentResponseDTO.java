package com.vishvendra.procart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponseDTO {

  private String paymentLink;
  private Long orderId;
  private String transactionTraceId;
  private String orderStatus;
  private String transactionStatus;
}
