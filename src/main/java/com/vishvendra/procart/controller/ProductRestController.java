package com.vishvendra.procart.controller;

import com.vishvendra.procart.model.PageResultResponse;
import com.vishvendra.procart.model.ProductDTO;
import com.vishvendra.procart.service.product.ProductService;
import com.vishvendra.procart.utils.response.ApiResponseSerializer;
import com.vishvendra.procart.utils.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductRestController {

  private final ProductService productService;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> createProduct(
      @RequestBody @Valid ProductDTO productDTO) {
    ProductDTO createdProduct = productService.createProduct(productDTO);
    return ApiResponseSerializer
        .successResponseSerializerBuilder()
        .withData(createdProduct)
        .withStatusCode(HttpStatus.CREATED)
        .build();
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> fetchProducts(
      @RequestParam(required = false) Long id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String sku,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    PageResultResponse<ProductDTO> products = productService.fetchProducts(id, name, sku,
        PageRequest.of(page, size));
    return ApiResponseSerializer
        .successResponseSerializerBuilder()
        .withData(products)
        .build();
  }

  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> updateProduct(@PathVariable Long id,
      @RequestBody @Valid ProductDTO productDTO) {
    ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
    return ApiResponseSerializer
        .successResponseSerializerBuilder()
        .withData(updatedProduct)
        .build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Response> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ApiResponseSerializer
        .successResponseSerializerBuilder()
        .withMessage("Product deleted successfully")
        .withStatusCode(HttpStatus.NO_CONTENT)
        .build();
  }
}

