package com.vishvendra.procart.service.product;

import com.vishvendra.procart.entities.AuditAction;
import com.vishvendra.procart.entities.Product;
import com.vishvendra.procart.entities.ProductCurrency;
import com.vishvendra.procart.event.AuditEvent;
import com.vishvendra.procart.event.EventDispatcher;
import com.vishvendra.procart.exception.ResourceNotFoundException;
import com.vishvendra.procart.mapper.ProductMapper;
import com.vishvendra.procart.model.PageResultResponse;
import com.vishvendra.procart.model.ProductDTO;
import com.vishvendra.procart.repository.CurrencyRepository;
import com.vishvendra.procart.repository.ProductRepository;
import com.vishvendra.procart.utils.PlatformSecurityContext;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("productService")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CurrencyRepository currencyRepository;
  private final ProductMapper productMapper;
  private final EventDispatcher eventDispatcher;

  @Override
  @Transactional
  public ProductDTO createProduct(ProductDTO productDTO) {
    ProductCurrency currency = currencyRepository.findById(productDTO.getCurrency().getId())
        .orElseThrow(() -> ResourceNotFoundException.create("Currency not found",
            String.format("Currency: %s not found", productDTO.getCurrency().getName())));
    Product product = productMapper.toEntity(productDTO);
    product.setCurrency(currency);
    product.setImage(null);
    productRepository.save(product);
//    handleEvent("Product " + product.getName() + " created.", AuditAction.ADD_PRODUCT);
    return productMapper.toResponseDTO(product);
  }

  @Override
  public PageResultResponse<ProductDTO> fetchProducts(Long id, String name, String sku,
      Pageable pageable) {
    Specification<Product> spec = ProductSpecifications.withFilters(id, name, sku);
    Page<Product> products = productRepository.findAll(spec, pageable);
    return mapToDTOList(products);
  }

  @Override
  @Transactional
  public ProductDTO updateProduct(Long id, ProductDTO productRequestDTO) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> ResourceNotFoundException.create("Product not found",
            String.format("Product with ID: %s not found", productRequestDTO.getName())));
    handleUpdate(product, productRequestDTO);
    Product updatedProduct = productRepository.save(product);
    handleEvent("Product " + product.getName() + " updated.", AuditAction.UPDATE_PRODUCT);
    return productMapper.toResponseDTO(updatedProduct);
  }

  @Override
  @Transactional
  public void deleteProduct(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> ResourceNotFoundException.create("Product not found",
            String.format("Product with ID: %s not found", id)));
    productRepository.delete(product);
    handleEvent("Product " + product.getName() + " deleted.", AuditAction.DELETE_PRODUCT);
  }


  private PageResultResponse<ProductDTO> mapToDTOList(Page<Product> products) {
    return PageResultResponse.of(
        products.map(productMapper::toResponseDTO).toList(),
        products.getSize(),
        products.getTotalElements(),
        products.getNumber(), products.isLast());
  }

  private Product handleUpdate(Product existingpProduct, ProductDTO productDTO) {
    if (Objects.nonNull(productDTO.getName()) && !productDTO.getName()
        .equals(existingpProduct.getName())) {
      existingpProduct.setName(productDTO.getName());
    }
    if (Objects.nonNull(productDTO.getDescription()) && !productDTO.getDescription()
        .equals(existingpProduct.getDescription())) {
      existingpProduct.setDescription(productDTO.getDescription());
    }
    if (Objects.nonNull(productDTO.getPrice()) && !productDTO.getPrice()
        .equals(existingpProduct.getPrice())) {
      existingpProduct.setPrice(productDTO.getPrice());
    }
    if (Objects.nonNull(productDTO.getImageUrl()) && !productDTO.getImageUrl()
        .equals(existingpProduct.getImageUrl())) {
      existingpProduct.setImageUrl(productDTO.getImageUrl());
    }
    if (Objects.nonNull(productDTO.getQuantity()) && !productDTO.getQuantity()
        .equals(existingpProduct.getQuantity())) {
      existingpProduct.setQuantity(productDTO.getQuantity());
    }
    if (Objects.nonNull(productDTO.getCurrency().getId()) && !productDTO.getCurrency().getId()
        .equals(existingpProduct.getCurrency().getId())) {
      ProductCurrency newCurrency = currencyRepository.findById(productDTO.getCurrency().getId())
          .orElseThrow(() -> ResourceNotFoundException.create("Currency not found",
              String.format("Currency: %s not found", productDTO.getCurrency().getName())));
      existingpProduct.setCurrency(newCurrency);
    }
    return existingpProduct;
  }

  private void handleEvent(String message, AuditAction auditAction) {
    eventDispatcher.dispatchEvent(
        new AuditEvent(message,
            PlatformSecurityContext.getLoggedUser().getUsername(),
            auditAction));
  }
}
