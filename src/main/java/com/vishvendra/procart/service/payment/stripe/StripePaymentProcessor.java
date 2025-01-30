package com.vishvendra.procart.service.payment.stripe;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.vishvendra.procart.exception.PaymentException;
import com.vishvendra.procart.service.payment.PaymentProcessor;
import com.vishvendra.procart.utils.payment.PaymentLinkRequest;
import com.vishvendra.procart.utils.payment.PaymentLinkResponse;
import com.vishvendra.procart.utils.payment.Webhook;
import com.vishvendra.procart.utils.payment.stripe.StripePaymentLinkRequest;
import com.vishvendra.procart.utils.payment.stripe.StripePaymentLinkResponse;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("stripePaymentProcessor")
@RequiredArgsConstructor
@Slf4j
public class StripePaymentProcessor implements
    PaymentProcessor<StripePaymentLinkRequest, StripePaymentLinkResponse, String> {

  @Override
  public PaymentLinkResponse<StripePaymentLinkResponse> createPaymentLink(
      PaymentLinkRequest<StripePaymentLinkRequest> tPaymentLinkRequest) throws PaymentException {
    Stripe.apiKey = "sk_test_51QmA77EnE7fi09wISJNSQO3QRJSE0LCWxdymUd4k343HUD3rI1MzXoRD3w94rfpJMSz2lLLHdfdXbI7PalxRqLEv00H9CEKvPQ";
    try {
      PaymentLinkResponse<StripePaymentLinkResponse> response = new PaymentLinkResponse<>();
      Session session = createCheckout(tPaymentLinkRequest.getLinkRequest());
      response.setPaymentLink(session.getUrl());
      response.setOtherData(new StripePaymentLinkResponse(session.getId()));
      return response;
    } catch (StripeException e) {
      log.error("Error while creating payment link", e);
      throw PaymentException.create("", "Error while creating payment link");
    }
  }

  private Session createCheckout(StripePaymentLinkRequest paymentLinkRequest)
      throws StripeException {
    BigDecimal finalAmount = BigDecimal.valueOf(paymentLinkRequest.getAmount() * 100);
    String traceId = paymentLinkRequest.getTraceId();
    long currentEpoch = Instant.now().getEpochSecond();
    long expiresAt = currentEpoch + 1800;
    SessionCreateParams params = SessionCreateParams.builder()
        .setSuccessUrl("https://yourwebsite.com/success")
        .setCancelUrl("https://yourwebsite.com/cancel")
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
        .setExpiresAt(expiresAt)
        .addLineItem(
            SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(paymentLinkRequest.getCurrencyCode())
                        .setUnitAmount(finalAmount.longValue())
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("ProCart_Payment_" + traceId)
                                .build()
                        )
                        .build()
                )
                .build()
        )
        .build();

    return Session.create(params);
  }

  @Override
  public void handleWebhook(Webhook<String> webhookResponse) {
//    Event event;
//
//    try {
//      event = com.stripe.net.Webhook.constructEvent(payload, sigHeader, webhookSecret);
//      Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
//      assert session != null;
//      final String status = session.getStatus();
//    } catch (SignatureVerificationException e) {
//      log.error("Webhook signature verification failed.", e);
//    }
//
  }
}
