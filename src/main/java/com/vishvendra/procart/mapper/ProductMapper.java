package com.vishvendra.procart.mapper;

import com.vishvendra.procart.entities.Product;
import com.vishvendra.procart.model.ProductDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductCurrencyMapper.class})
public interface ProductMapper {
  Product toEntity(ProductDTO productRequestDTO);

//  @Mapping(source = "currency.name", target = "currency")
  ProductDTO toResponseDTO(Product product);

  List<ProductDTO> toResponseDTOList(List<Product> products);
}

