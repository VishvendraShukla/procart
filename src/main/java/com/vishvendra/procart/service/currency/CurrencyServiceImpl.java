package com.vishvendra.procart.service.currency;

import com.vishvendra.procart.entities.ProductCurrency;
import com.vishvendra.procart.exception.ResourceNotFoundException;
import com.vishvendra.procart.mapper.ProductCurrencyMapper;
import com.vishvendra.procart.model.PageResultResponse;
import com.vishvendra.procart.model.ProductCurrencyDTO;
import com.vishvendra.procart.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("currencyService")
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

  private final CurrencyRepository currencyRepository;
  private final ProductCurrencyMapper productCurrencyMapper;

  @Override
  @Transactional
  public ProductCurrencyDTO createCurrency(ProductCurrencyDTO productCurrencyDTO) {
    ProductCurrency productCurrency = productCurrencyMapper.toEntity(productCurrencyDTO);
    ProductCurrency savedCurrency = currencyRepository.save(productCurrency);
    return productCurrencyMapper.toDto(savedCurrency);
  }

  @Override
  public PageResultResponse<ProductCurrencyDTO> getAllCurrencies(String currencyCode,
      Pageable pageable) {
    Specification<ProductCurrency> specification = CurrencySpecifications.withFilters(currencyCode);
    Page<ProductCurrency> currencies = currencyRepository.findAll(specification, pageable);
    return mapToDTOList(currencies);
  }

  @Override
  public ProductCurrencyDTO getCurrencyById(Long id) {
    ProductCurrency currency = currencyRepository.findById(id)
        .orElseThrow(() -> ResourceNotFoundException.create("Currency not found",
            "Currency not found with ID: " + id));
    return productCurrencyMapper.toDto(currency);
  }

  @Override
  @Transactional
  public ProductCurrencyDTO updateCurrency(Long id, ProductCurrencyDTO productCurrencyDTO) {
    ProductCurrency existingCurrency = currencyRepository.findById(id)
        .orElseThrow(() -> ResourceNotFoundException.create("Currency not found",
            "Currency not found with ID: " + id));
    existingCurrency.setName(productCurrencyDTO.getName());
    existingCurrency.setCode(productCurrencyDTO.getCode());
    existingCurrency.setSymbol(productCurrencyDTO.getSymbol());
    ProductCurrency updatedCurrency = currencyRepository.save(existingCurrency);
    return productCurrencyMapper.toDto(updatedCurrency);
  }

  @Override
  @Transactional
  public void deleteCurrency(Long id) {
    ProductCurrency currency = currencyRepository.findById(id)
        .orElseThrow(() -> ResourceNotFoundException.create("Currency not found",
            "Currency not found with ID: " + id));
    currencyRepository.delete(currency);
  }

  private PageResultResponse<ProductCurrencyDTO> mapToDTOList(Page<ProductCurrency> currencies) {
    return PageResultResponse.of(
        currencies.map(productCurrencyMapper::toDto).toList(),
        currencies.getSize(),
        currencies.getTotalElements(),
        currencies.getNumber(),
        currencies.isLast());
  }
}

