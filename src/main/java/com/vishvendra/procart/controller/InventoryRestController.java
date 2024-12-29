package com.vishvendra.procart.controller;

import com.vishvendra.procart.model.InventoryDTO;
import com.vishvendra.procart.service.inventory.InventoryService;
import com.vishvendra.procart.utils.response.ApiResponseSerializer;
import com.vishvendra.procart.utils.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/inventories")
@RequiredArgsConstructor
public class InventoryRestController {

  private final InventoryService inventoryService;

  @GetMapping
  public ResponseEntity<Response> findByFilters(
      @RequestParam(required = false) Long inventoryId,
      @RequestParam(required = false) Long productId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withStatusCode(HttpStatus.OK)
        .withData(inventoryService.findWithSpecifications(inventoryId, productId,
            PageRequest.of(page, size)))
        .build();
  }

  @PostMapping
  public ResponseEntity<Response> createInventory(@Valid @RequestBody InventoryDTO inventoryDTO) {
    inventoryService.create(inventoryDTO);
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withStatusCode(HttpStatus.CREATED)
        .withMessage("Inventory created successfully!")
        .build();
  }

  @PutMapping
  public ResponseEntity<Response> updateInventory(@Valid @RequestBody InventoryDTO inventoryDTO) {
    inventoryService.update(inventoryDTO);
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withStatusCode(HttpStatus.CREATED)
        .withMessage("Inventory updated successfully!")
        .build();
  }
}
