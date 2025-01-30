package com.vishvendra.procart.service.order;

import com.vishvendra.procart.entities.Cart;
import com.vishvendra.procart.entities.CartStatus;
import com.vishvendra.procart.entities.Charge;
import com.vishvendra.procart.entities.ChargeAppliesOn;
import com.vishvendra.procart.entities.Order;
import com.vishvendra.procart.entities.OrderStatus;
import com.vishvendra.procart.entities.Transaction;
import com.vishvendra.procart.entities.TransactionStatus;
import com.vishvendra.procart.entities.User;
import com.vishvendra.procart.exception.OrderException;
import com.vishvendra.procart.exception.PaymentException;
import com.vishvendra.procart.exception.ResourceNotFoundException;
import com.vishvendra.procart.model.ChargeDTO;
import com.vishvendra.procart.model.CreateOrderDTO;
import com.vishvendra.procart.model.PaymentResponseDTO;
import com.vishvendra.procart.repository.CartRepository;
import com.vishvendra.procart.repository.ChargeRepository;
import com.vishvendra.procart.repository.OrderRepository;
import com.vishvendra.procart.repository.UserRepository;
import com.vishvendra.procart.service.charge.ChargeCalculationService;
import com.vishvendra.procart.service.payment.PaymentProcessor;
import com.vishvendra.procart.service.transaction.TransactionService;
import com.vishvendra.procart.utils.PlatformSecurityContext;
import com.vishvendra.procart.utils.payment.PaymentLinkRequest;
import com.vishvendra.procart.utils.payment.PaymentLinkResponse;
import com.vishvendra.procart.utils.payment.TransactionDTO;
import com.vishvendra.procart.utils.payment.stripe.StripePaymentLinkRequest;
import com.vishvendra.procart.utils.payment.stripe.StripePaymentLinkResponse;
import com.vishvendra.procart.utils.securitymodel.CustomUser;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("orderService")
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

  private final CartRepository cartRepository;
  private final UserRepository userRepository;
  private final PaymentProcessor paymentProcessor;
  private final ChargeRepository chargeRepository;
  private final TransactionService transactionService;
  private final OrderRepository orderRepository;
  private final ChargeCalculationService chargeCalculationService;


  @Override
  @Transactional
  public PaymentResponseDTO createOrder(CreateOrderDTO createOrderDTO) throws OrderException {
    User user = findLoggedUser();
    Long cartId = createOrderDTO.getCartId();
    List<Charge> charges = validateChargesAndReturnList(createOrderDTO.getCharges());
    if (Objects.isNull(user.getProfileDetails()) || Objects.isNull(
        user.getProfileDetails().getBillingAddress())) {
      throw OrderException.create("Billing Address is not set.", "Billing Address is null.");
    }

    Cart cart = cartRepository.findById(cartId).orElseThrow(() -> {
      String message = String.format("Cart does not exists for Id: %s", cartId);
      return OrderException.create(message, message);
    });

    if (cart.getCartStatus() != CartStatus.COMPLETED) {
      String message = String.format("Cart is not completed for Id: %s", cartId);
      throw OrderException.create("Cart must be completed before placing on order.", message);
    }

    if (cart.getItems().isEmpty()) {
      throw OrderException.create("Cart is empty.", "Cart is empty.");
    }

    String currencyCode = cart.getItems().stream().findAny().get().getProduct().getCurrency()
        .getCode();

    BigDecimal finalAmount = chargeCalculationService.calculateTotalAmount(cart, charges);
    Transaction transaction = createTransaction(finalAmount, currencyCode, user, cart,
        charges);

    Order order = createPendingOrder(cart, user, transaction, finalAmount);

    try {
      PaymentLinkResponse<StripePaymentLinkResponse> paymentLink = paymentProcessor.createPaymentLink(
          createPaymentLinkRequest(finalAmount, currencyCode, order.getId(),
              transaction.getTraceId()));
      transaction.setReferenceId(paymentLink.getOtherData().getPaymentLinkReferenceId());
      transactionService.updateTransaction(transaction);
      return new PaymentResponseDTO(
          paymentLink.getPaymentLink(),
          order.getId(),
          transaction.getTraceId(),
          order.getStatus().name(),
          transaction.getStatus().name()
      );
    } catch (Exception e) {
      log.error("Payment link creation failed for order: {}", order.getId(), e);
      throw OrderException.create("Payment link creation failed.", e.getMessage());
    }
  }

  @Override
  @Transactional
  public void cancelOrder(Long orderId) throws OrderException {

  }

  @Override
  @Transactional
  public void completeOrder(Long orderId) throws OrderException {

  }

  private User findLoggedUser() {
    CustomUser customUser = PlatformSecurityContext.getLoggedUser();
    return userRepository.findById(customUser.getUserId())
        .orElseThrow(() -> ResourceNotFoundException.create(
            "User not found",
            String.format("User with ID: %s not found", customUser.getUserId())));
  }

  private PaymentLinkRequest<StripePaymentLinkRequest> createPaymentLinkRequest(
      BigDecimal amount,
      String currencyCode,
      Long orderId,
      String traceId) {
    PaymentLinkRequest<StripePaymentLinkRequest> paymentLinkRequest = new PaymentLinkRequest<>();
    StripePaymentLinkRequest stripePaymentLinkRequest = new StripePaymentLinkRequest();
    stripePaymentLinkRequest.setCurrencyCode(currencyCode);
    stripePaymentLinkRequest.setAmount(amount.longValue());
    stripePaymentLinkRequest.setOrderId(orderId.toString());
    stripePaymentLinkRequest.setTraceId(traceId);
    paymentLinkRequest.setLinkRequest(stripePaymentLinkRequest);
    return paymentLinkRequest;
  }

  private List<Charge> validateChargesAndReturnList(List<ChargeDTO> chargeDTOList) {
    if (Objects.isNull(chargeDTOList) || chargeDTOList.isEmpty()) {
      return null;
    }
    List<Charge> charges = chargeRepository.findAllById(
        chargeDTOList.stream().map(ChargeDTO::getId).toList());

    charges.stream()
        .filter(charge -> charge.getChargeAppliesOn() != ChargeAppliesOn.TRANSACTION)
        .findFirst().ifPresent(charge -> {
              throw OrderException.create("Invalid request data.",
                  "Charge Applies on does not matches.");
            }
        );
    return charges;
  }

  @Override
  public Order upsert(Order order) {
    return orderRepository.save(order);
  }

  private Order createPendingOrder(Cart cart, User user, Transaction transaction, BigDecimal totalAmount) {
    Order order = new Order();
    order.setCart(cart);
    order.setUser(user);
    order.setStatus(OrderStatus.PENDING);
    order.setTransaction(transaction);
    order.setTotalAmount(totalAmount);
    return upsert(order);
  }



  private Transaction createTransaction(
      BigDecimal amount,
      String currencyCode,
      User user,
      Cart cart,
      List<Charge> charges) {
    return transactionService.createTransaction(
        new TransactionDTO(
            currencyCode,
            amount,
            null,
            null,
            "stripe",
            null,
            TransactionStatus.PENDING,
            cart,
            user,
            charges));
  }


}
