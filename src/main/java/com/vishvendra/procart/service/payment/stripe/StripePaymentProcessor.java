package com.vishvendra.procart.service.payment.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.vishvendra.procart.entities.Transaction;
import com.vishvendra.procart.entities.TransactionStatus;
import com.vishvendra.procart.exception.PaymentException;
import com.vishvendra.procart.service.configuration.ConfigurationService;
import com.vishvendra.procart.service.payment.PaymentProcessor;
import com.vishvendra.procart.service.transaction.TransactionService;
import com.vishvendra.procart.utils.payment.PaymentLinkRequest;
import com.vishvendra.procart.utils.payment.PaymentLinkResponse;
import com.vishvendra.procart.utils.payment.Webhook;
import com.vishvendra.procart.utils.payment.stripe.StripeCheckoutSessionWebhookResponse;
import com.vishvendra.procart.utils.payment.stripe.StripePaymentLinkRequest;
import com.vishvendra.procart.utils.payment.stripe.StripePaymentLinkResponse;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("stripePaymentProcessor")
@RequiredArgsConstructor
@Slf4j
public class StripePaymentProcessor implements
    PaymentProcessor<StripePaymentLinkRequest, StripePaymentLinkResponse, StripeCheckoutSessionWebhookResponse> {

  private final TransactionService transactionService;
  private final ConfigurationService configurationService;
  private String stripeApiKey;
  private String stripeWebhookSecret;

  @Override
  public PaymentLinkResponse<StripePaymentLinkResponse> createPaymentLink(
      PaymentLinkRequest<StripePaymentLinkRequest> tPaymentLinkRequest) throws PaymentException {
    if (Objects.isNull(this.stripeApiKey)) {
      initiateData();
    }
    Stripe.apiKey = this.stripeApiKey;
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
  public void handleWebhook(Webhook<StripeCheckoutSessionWebhookResponse> webhookResponse)
      throws Exception {
    if (Objects.isNull(this.stripeApiKey)) {
      initiateData();
    }
    Stripe.apiKey = this.stripeApiKey;
    String payload = webhookResponse.getWebhookResponse().getPayload();
    String sigHeader = webhookResponse.getWebhookResponse().getSigHeader();
    Event event = com.stripe.net.Webhook.constructEvent(
        payload, sigHeader, this.stripeWebhookSecret
    );
    log.info("Event: {}", event.getType());
    log.info("RequestBody: {}", payload);
    if ("checkout.session.completed".equalsIgnoreCase(event.getType()) ||
        "checkout.session.async_payment_succeeded".equalsIgnoreCase(event.getType())) {
      Optional<StripeObject> stripeObjectOptional = (Optional<StripeObject>) event.getDataObjectDeserializer()
          .getObject();
      if (stripeObjectOptional.isPresent()) {
        Session session = (Session) stripeObjectOptional.get();
        Transaction transaction = transactionService.getTransactionByReferenceId(session.getId());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionService.updateTransaction(transaction);
      }
    } else if ("checkout.session.expired".equalsIgnoreCase(event.getType()) ||
        "checkout.session.async_payment_failed".equalsIgnoreCase(event.getType())) {
      Optional<StripeObject> stripeObjectOptional = (Optional<StripeObject>) event.getDataObjectDeserializer()
          .getObject();
      if (stripeObjectOptional.isPresent()) {
        Session session = (Session) stripeObjectOptional.get();
        Transaction transaction = transactionService.getTransactionByReferenceId(session.getId());
        transaction.setStatus(TransactionStatus.FAILED);
        transactionService.updateTransaction(transaction);
      }
    }
  }

  private void initiateData() {
    this.stripeApiKey = this.configurationService.getConfiguration("STRIPE_API_KEY").getValue();
    this.stripeWebhookSecret = this.configurationService.getConfiguration("STRIPE_WEBHOOK_SECRET")
        .getValue();
  }
}
