package com.vishvendra.procart.service.inventory;

import com.vishvendra.procart.entities.Product;
import com.vishvendra.procart.exception.InventoryExistsException;
import com.vishvendra.procart.model.InventoryDTO;
import com.vishvendra.procart.model.InventoryResponseDTO;
import com.vishvendra.procart.model.PageResultResponse;
import org.springframework.data.domain.Pageable;

public interface InventoryService {

  PageResultResponse<InventoryResponseDTO> findWithSpecifications(Long inventoryId, Long productId, Pageable pageable);

  void create(InventoryDTO inventoryDTO) throws InventoryExistsException;

  void update(InventoryDTO inventoryDTO) throws InventoryExistsException;

  void reserveStock(Product product, Long quantity);

  boolean checkAvailableStock(Product product, Long quantity);

}
