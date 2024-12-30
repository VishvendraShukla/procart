package com.vishvendra.procart.service.inventory;

import com.vishvendra.procart.entities.AuditAction;
import com.vishvendra.procart.entities.Inventory;
import com.vishvendra.procart.entities.Product;
import com.vishvendra.procart.event.AuditEvent;
import com.vishvendra.procart.event.EventDispatcher;
import com.vishvendra.procart.exception.InsufficientStockException;
import com.vishvendra.procart.exception.InventoryExistsException;
import com.vishvendra.procart.exception.ResourceNotFoundException;
import com.vishvendra.procart.mapper.InventoryMapper;
import com.vishvendra.procart.model.InventoryDTO;
import com.vishvendra.procart.model.InventoryResponseDTO;
import com.vishvendra.procart.model.PageResultResponse;
import com.vishvendra.procart.repository.InventoryRepository;
import com.vishvendra.procart.repository.ProductRepository;
import com.vishvendra.procart.utils.PlatformSecurityContext;
import com.vishvendra.procart.utils.securitymodel.CustomUser;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("inventoryService")
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

  private final InventoryRepository inventoryRepository;
  private final ProductRepository productRepository;
  private final InventoryMapper inventoryMapper;
  private final EventDispatcher eventDispatcher;

  @Override
  public boolean checkAvailableStock(Product product, Long quantity) {
    Inventory inventory = inventoryRepository.findByProductWithLock(product);
    return inventory != null && inventory.getAvailableStock() >= quantity;
  }

  @Override
  public PageResultResponse<InventoryResponseDTO> findWithSpecifications(Long inventoryId,
      Long productId, Pageable pageable) {
    Product product = productId != null ? findProductByIdOrThrowException(productId) : null;
    Specification<Inventory> inventorySpecification = InventorySpecifications.withFilters(
        inventoryId, product);
    Page<Inventory> inventoryPage = inventoryRepository.findAll(inventorySpecification, pageable);
    return mapToDTOList(inventoryPage);
  }

  @Override
  @Transactional
  public void create(InventoryDTO inventoryDTO) throws InventoryExistsException {
    if (Objects.nonNull(inventoryDTO.getId())) {
      String message = String.format("Inventory already exists for with ID: %s",
          inventoryDTO.getId());
      throw InventoryExistsException.create(message, message);
    }
    Product product = findProductByIdOrThrowException(inventoryDTO.getProductId());

    inventoryRepository.findByProduct(product).ifPresent(inventory -> {
      String message = String.format("Inventory already exists for product: %s", product.getName());
      throw InventoryExistsException.create(message, message);
    });

    Inventory inventory = new Inventory();
    inventory.setProduct(product);
    inventory.setTotalStock(inventoryDTO.getQuantity());
    inventory.setAvailableStock(inventoryDTO.getQuantity());
    inventory.setLockedStock(0L);
    inventoryRepository.save(inventory);
    CustomUser loggedUser = PlatformSecurityContext.getLoggedUser();
    eventDispatcher.dispatchEvent(
        new AuditEvent("Inventory created for product: " + product.getName() + " with quantity: "
            + inventoryDTO.getQuantity().toString(),
            loggedUser.getUsername(), AuditAction.CREATE_INVENTORY));
  }

  @Override
  @Transactional
  public void update(InventoryDTO inventoryDTO) throws InventoryExistsException {
    if (Objects.isNull(inventoryDTO.getId())) {
      throw InventoryExistsException.create("Cannot Update inventory without ID",
          "Cannot Update inventory without ID");
    }
    Product product = findProductByIdOrThrowException(inventoryDTO.getProductId());

    Inventory inventory = inventoryRepository.findByProduct(product).orElseThrow(() -> {
      String message = String.format("Inventory does not exists for product: %s",
          product.getName());
      return InventoryExistsException.create(message, message);
    });
    Long currentStock = inventory.getTotalStock();
    if (inventoryDTO.getQuantity() < 0) {
      Long quantityToReduce = Math.abs(inventoryDTO.getQuantity());
      if (currentStock < quantityToReduce) {
        throw InsufficientStockException.create(
            "You can only reduce " + inventory.getAvailableStock() + " units for this.",
            "Not enough stock for product in inventory to reduce with inventoryId: "
                + inventory.getId());
      }
      inventory.setTotalStock(Math.abs(currentStock - quantityToReduce));
    } else {
      inventory.setTotalStock(currentStock + inventoryDTO.getQuantity());
    }
    inventoryRepository.save(inventory);
    CustomUser loggedUser = PlatformSecurityContext.getLoggedUser();
    eventDispatcher.dispatchEvent(
        new AuditEvent("Inventory updated for product: " + product.getName(),
            loggedUser.getUsername(), AuditAction.UPDATE_INVENTORY));
  }

  @Override
  @Transactional
  public void reserveStock(Product product, Long quantity) {
    Inventory inventory = inventoryRepository.findByProductWithLock(product);
    if (inventory.getAvailableStock() < quantity) {
      throw InsufficientStockException.create(
          "Sorry, we are currently out of " + product.getName(),
          "Not enough locked stock for product: " + product.getName());
    }

    inventory.setAvailableStock(inventory.getAvailableStock() - quantity);
    inventory.setLockedStock(inventory.getLockedStock() + quantity);

    inventoryRepository.save(inventory);
  }

  @Transactional
  public void releaseStock(Product product, Long quantity) {
    Inventory inventory = inventoryRepository.findByProductWithLock(product);
    if (inventory.getLockedStock() < quantity) {
      throw InsufficientStockException.create(
          "Sorry, we are currently out of " + product.getName(),
          "Not enough locked stock for product: " + product.getName());
    }
    inventory.setLockedStock(inventory.getLockedStock() - quantity);
    inventory.setTotalStock(inventory.getTotalStock() - quantity);
    inventoryRepository.save(inventory);
  }

  private Product findProductByIdOrThrowException(Long productId) {
    return productRepository.findById(productId).orElseThrow(()
        -> ResourceNotFoundException.create("Product not found",
        String.format("Product with ID: %s not found", productId)));
  }

  private PageResultResponse<InventoryResponseDTO> mapToDTOList(Page<Inventory> inventories) {
    return PageResultResponse.of(
        inventories.map(inventoryMapper::toDTO).toList(),
        inventories.getSize(),
        inventories.getTotalElements(),
        inventories.getNumber(),
        inventories.isLast());
  }
}
