package com.vishvendra.procart.mapper;

import com.vishvendra.procart.entities.Product;
import com.vishvendra.procart.model.ProductDTO;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductCurrencyMapper.class, ChargeMapper.class})
public interface ProductMapper {

  Product toEntity(ProductDTO productRequestDTO);

  ProductDTO toResponseDTO(Product product);

  List<ProductDTO> toResponseDTOList(List<Product> products);
}

