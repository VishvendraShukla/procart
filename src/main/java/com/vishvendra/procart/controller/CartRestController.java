package com.vishvendra.procart.controller;

import com.vishvendra.procart.model.CreateCartDTO;
import com.vishvendra.procart.model.UpdateCartDTO;
import com.vishvendra.procart.service.cart.CartService;
import com.vishvendra.procart.utils.response.ApiResponseSerializer;
import com.vishvendra.procart.utils.response.Response;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class CartRestController {

  private final CartService cartService;

  @PostMapping
  public ResponseEntity<Response> createCart(@RequestBody CreateCartDTO createCartDTO) {
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withStatusCode(HttpStatus.OK)
        .withData(Map.of("cartId", this.cartService.createCart(createCartDTO))).build();
  }

  @PutMapping
  public ResponseEntity<Response> updateCart(@RequestBody UpdateCartDTO updateCartDTO) {
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withStatusCode(HttpStatus.OK)
        .withData(Map.of("cartId", this.cartService.addToCart(updateCartDTO))).build();
  }
}
