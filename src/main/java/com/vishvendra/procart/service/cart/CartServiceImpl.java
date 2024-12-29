package com.vishvendra.procart.service.cart;

import com.vishvendra.procart.entities.Cart;
import com.vishvendra.procart.entities.CartStatus;
import com.vishvendra.procart.entities.Items;
import com.vishvendra.procart.entities.Product;
import com.vishvendra.procart.entities.User;
import com.vishvendra.procart.exception.CartAlreadyActiveException;
import com.vishvendra.procart.exception.InsufficientStockException;
import com.vishvendra.procart.exception.ResourceNotFoundException;
import com.vishvendra.procart.model.CartData;
import com.vishvendra.procart.model.CreateCartDTO;
import com.vishvendra.procart.model.UpdateCartDTO;
import com.vishvendra.procart.repository.CartRepository;
import com.vishvendra.procart.repository.ProductRepository;
import com.vishvendra.procart.repository.UserRepository;
import com.vishvendra.procart.service.inventory.InventoryService;
import com.vishvendra.procart.utils.securitymodel.CustomUser;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("cartService")
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

  private final InventoryService inventoryService;

  private final CartRepository cartRepository;

  private final ProductRepository productRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public Long createCart(CreateCartDTO createCartDTO) {
    CartData returnValues = validateDataAndReturnValues(createCartDTO);
    if (!inventoryService.checkAvailableStock(returnValues.getProduct(),
        createCartDTO.getQuantity())) {
      throw InsufficientStockException.create(
          "Sorry, we are currently out of " + returnValues.getProduct().getName(),
          "Not enough locked stock for product: " + returnValues.getProduct().getName());
    }
    cartRepository.findActiveCartByUser(returnValues.getUser(), CartStatus.ACTIVE).ifPresent(
        cart -> {
          throw CartAlreadyActiveException.create("There is already a cart active.",
              String.format("Active Cart found for user: %s", returnValues.getUser().getId()));
        }
    );
    inventoryService.reserveStock(returnValues.getProduct(), createCartDTO.getQuantity());
    Cart cart = new Cart();
    cart.setUser(returnValues.getUser());
    cart.setCartStatus(CartStatus.ACTIVE);
    Items item = new Items();
    item.setProduct(returnValues.getProduct());
    item.setQuantity(createCartDTO.getQuantity());
    item.setPrice(returnValues.getProduct().getPrice());
    cart.setItems(List.of(item));
    cart.setTotalPrice(BigDecimal.valueOf(createCartDTO.getQuantity())
        .multiply(returnValues.getProduct().getPrice()));
    return cartRepository.save(cart).getId();
  }

  @Override
  @Transactional
  public Long addToCart(UpdateCartDTO updateCartDTO) {
    CartData returnValues = validateDataAndReturnValues(updateCartDTO);
    return addToCart(returnValues.getUser(), returnValues.getProduct(),
        updateCartDTO.getQuantity()).getId();
  }


  private CartData validateDataAndReturnValues(CreateCartDTO createCartDTO) {
    Product product = productRepository.findById(createCartDTO.getProductId()).orElseThrow(()
        -> ResourceNotFoundException.create("Product not found",
        String.format("Product with ID: %s not found", createCartDTO.getProductId())));
    CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    User user = userRepository.findById(customUser.getUserId())
        .orElseThrow(() -> ResourceNotFoundException.create(
            "User not found",
            String.format("User with ID: %s not found", customUser.getUserId())));
    return new CartData(product, user);
  }

  public Cart addToCart(User user, Product product, Long quantity) {
    if (!inventoryService.checkAvailableStock(product, quantity)) {
      throw InsufficientStockException.create(
          "Sorry, we are currently out of " + product.getName(),
          "Not enough locked stock for product: " + product.getName());
    }

    inventoryService.reserveStock(product, quantity);

    Cart cart = cartRepository.findActiveCartByUser(user, CartStatus.ACTIVE).orElse(null);
    if (Objects.isNull(cart)) {
      cart = new Cart();
      cart.setUser(user);
      cart.setItems(new ArrayList<>());
      cart.setCartStatus(CartStatus.ACTIVE);
      cartRepository.save(cart);
    }

    Optional<Items> existingItem = cart.getItems().stream()
        .filter(item -> item.getProduct().equals(product))
        .findFirst();

    if (existingItem.isPresent()) {
      Items item = existingItem.get();
      item.setQuantity(item.getQuantity() + quantity);
    } else {
      Items newItem = new Items();
      newItem.setProduct(product);
      newItem.setQuantity(quantity);
      newItem.setPrice(product.getPrice());
      cart.getItems().add(newItem);
    }

    cart.setTotalPrice(cart.getItems().stream()
        .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add));

    return cartRepository.save(cart);
  }
}

