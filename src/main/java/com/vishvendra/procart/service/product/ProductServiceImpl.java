package com.vishvendra.procart.service.product;

import com.vishvendra.procart.entities.Product;
import com.vishvendra.procart.entities.ProductCurrency;
import com.vishvendra.procart.exception.ResourceNotFoundException;
import com.vishvendra.procart.mapper.ProductMapper;
import com.vishvendra.procart.model.PageResultResponse;
import com.vishvendra.procart.model.ProductCurrencyDTO;
import com.vishvendra.procart.model.ProductDTO;
import com.vishvendra.procart.repository.CurrencyRepository;
import com.vishvendra.procart.repository.ProductRepository;
import java.util.List;
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
    return productMapper.toResponseDTO(product);
  }

  @Override
  public PageResultResponse<ProductDTO> fetchProducts(Long id, String name, String sku, Pageable pageable) {
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
    productMapper.toEntity(productRequestDTO);
    Product updatedProduct = productRepository.save(product);
    return productMapper.toResponseDTO(updatedProduct);
  }

  @Override
  @Transactional
  public void deleteProduct(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> ResourceNotFoundException.create("Product not found",
            String.format("Product with ID: %s not found", id)));
    productRepository.delete(product);
  }


  private PageResultResponse<ProductDTO> mapToDTOList(Page<Product> products) {
    return PageResultResponse.of(
        products.map(productMapper::toResponseDTO).toList(),
        products.getSize(),
        products.getTotalElements(),
        products.getNumber(), products.isLast());
  }
}
