package com.vishvendra.procart.service.product;

import com.vishvendra.procart.model.PageResultResponse;
import com.vishvendra.procart.model.ProductDTO;
import org.springframework.data.domain.Pageable;

public interface ProductService {


  ProductDTO createProduct(ProductDTO productDTO);

  PageResultResponse<ProductDTO> fetchProducts(Long id, String name, String sku, Pageable pageable);

  ProductDTO updateProduct(Long id, ProductDTO productRequestDTO);

  void deleteProduct(Long id);

  void associateCharge(Long productId, Long chargeId);

}
