package com.vishvendra.procart.service.order;

import com.vishvendra.procart.entities.Order;
import com.vishvendra.procart.exception.OrderException;
import com.vishvendra.procart.model.CreateOrderDTO;
import com.vishvendra.procart.model.PaymentResponseDTO;

public interface OrderService {

  PaymentResponseDTO createOrder(CreateOrderDTO createOrderDTO) throws OrderException;

  void cancelOrder(Long orderId) throws OrderException;

  void completeOrder(Long orderId) throws OrderException;

  Order upsert(Order order);
}
