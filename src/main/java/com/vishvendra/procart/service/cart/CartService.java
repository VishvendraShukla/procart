package com.vishvendra.procart.service.cart;

import com.vishvendra.procart.model.CreateCartDTO;
import com.vishvendra.procart.model.UpdateCartDTO;

public interface CartService {

  Long createCart(CreateCartDTO createCartDTO);

  Long addToCart(UpdateCartDTO createCartDTO);

}
