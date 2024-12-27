package com.vishvendra.procart.service.currency;

import com.vishvendra.procart.model.PageResultResponse;
import com.vishvendra.procart.model.ProductCurrencyDTO;
import org.springframework.data.domain.Pageable;

public interface CurrencyService {

  ProductCurrencyDTO createCurrency(ProductCurrencyDTO productCurrencyDTO);

  PageResultResponse<ProductCurrencyDTO> getAllCurrencies(String currencyCode, Pageable pageable);

  ProductCurrencyDTO getCurrencyById(Long id);

  ProductCurrencyDTO updateCurrency(Long id, ProductCurrencyDTO productCurrencyDTO);

  void deleteCurrency(Long id);
}
