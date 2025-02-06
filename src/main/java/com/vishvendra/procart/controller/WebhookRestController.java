package com.vishvendra.procart.controller;

import com.stripe.exception.SignatureVerificationException;
import com.vishvendra.procart.service.payment.PaymentProcessor;
import com.vishvendra.procart.utils.payment.stripe.StripeCheckoutSessionWebhookResponse;
import com.vishvendra.procart.utils.response.ApiResponseSerializer;
import com.vishvendra.procart.utils.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/webhook")
@RequiredArgsConstructor
@Slf4j
public class WebhookRestController {

  private final PaymentProcessor paymentProcessor;

  @PostMapping(value = "stripe")
  public ResponseEntity<Response> handleWebhook(
      @RequestBody String payload,
      @RequestHeader("Stripe-Signature") String sigHeader) {
    com.vishvendra.procart.utils.payment.Webhook<StripeCheckoutSessionWebhookResponse> webhookResponse =
        new com.vishvendra.procart.utils.payment.Webhook<StripeCheckoutSessionWebhookResponse>();
    webhookResponse.setWebhookResponse(
        new StripeCheckoutSessionWebhookResponse(payload, sigHeader));

    try {
      paymentProcessor.handleWebhook(webhookResponse);
      return ApiResponseSerializer.successResponseSerializerBuilder()
          .withMessage("Webhook received")
          .withStatusCode(HttpStatus.ACCEPTED)
          .build();
    } catch (SignatureVerificationException e) {
      return ApiResponseSerializer.successResponseSerializerBuilder()
          .withMessage("Invalid signature")
          .withStatusCode(HttpStatus.UNAUTHORIZED)
          .build();
    } catch (Exception e) {
      return ApiResponseSerializer.successResponseSerializerBuilder()
          .withMessage("Webhook error")
          .withStatusCode(HttpStatus.BAD_REQUEST)
          .build();
    }
  }

}
