package com.vishvendra.procart.mapper;

import com.vishvendra.procart.entities.ProductCurrency;
import com.vishvendra.procart.model.ProductCurrencyDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductCurrencyMapper {

  ProductCurrency toEntity(ProductCurrencyDTO productCurrencyDTO);

  ProductCurrencyDTO toDto(ProductCurrency productCurrency);
}
