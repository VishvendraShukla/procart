package com.vishvendra.procart.controller;


import com.vishvendra.procart.model.CreateOrderDTO;
import com.vishvendra.procart.model.PaymentResponseDTO;
import com.vishvendra.procart.service.order.OrderService;
import com.vishvendra.procart.utils.response.ApiResponseSerializer;
import com.vishvendra.procart.utils.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderRestController {

  private final OrderService orderService;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> createOrders(
      @RequestBody CreateOrderDTO createOrderDTO) {

    PaymentResponseDTO responseDTO = this.orderService.createOrder(createOrderDTO);

    return ApiResponseSerializer
        .successResponseSerializerBuilder()
        .withData(responseDTO)
        .withMessage("Order created successfully")
        .build();
  }

}
