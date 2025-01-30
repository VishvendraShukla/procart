package com.vishvendra.procart.service.payment;

import com.vishvendra.procart.exception.PaymentException;
import com.vishvendra.procart.utils.payment.PaymentLinkRequest;
import com.vishvendra.procart.utils.payment.PaymentLinkResponse;
import com.vishvendra.procart.utils.payment.Webhook;

public interface PaymentProcessor<RE, RP, WE> {

  PaymentLinkResponse<RP> createPaymentLink(PaymentLinkRequest<RE> tPaymentLinkRequest) throws PaymentException;

  void handleWebhook(Webhook<WE> webhookResponse);

}
