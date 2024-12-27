package com.vishvendra.procart.controller;

import com.vishvendra.procart.model.PageResultResponse;
import com.vishvendra.procart.model.ProductCurrencyDTO;
import com.vishvendra.procart.service.currency.CurrencyService;
import com.vishvendra.procart.utils.response.ApiResponseSerializer;
import com.vishvendra.procart.utils.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
public class ProductCurrencyRestController {

  private final CurrencyService currencyService;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> createCurrency(
      @RequestBody @Valid ProductCurrencyDTO productCurrencyDTO) {
    ProductCurrencyDTO createdCurrency = currencyService.createCurrency(productCurrencyDTO);
    return ApiResponseSerializer
        .successResponseSerializerBuilder()
        .withData(createdCurrency)
        .withStatusCode(HttpStatus.CREATED)
        .build();
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> getAllCurrencies(
      @RequestParam String currencyCode,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    PageResultResponse<ProductCurrencyDTO> currencies = currencyService.getAllCurrencies(
        currencyCode,
        PageRequest.of(page, size));
    return ApiResponseSerializer
        .successResponseSerializerBuilder()
        .withData(currencies)
        .withStatusCode(HttpStatus.FOUND)
        .build();
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> getCurrencyById(@PathVariable Long id) {
    ProductCurrencyDTO currency = currencyService.getCurrencyById(id);
    return ApiResponseSerializer
        .successResponseSerializerBuilder()
        .withData(currency)
        .withStatusCode(HttpStatus.FOUND)
        .build();
  }

  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> updateCurrency(@PathVariable Long id,
      @RequestBody @Valid ProductCurrencyDTO productCurrencyDTO) {
    ProductCurrencyDTO updatedCurrency = currencyService.updateCurrency(id, productCurrencyDTO);
    return ApiResponseSerializer
        .successResponseSerializerBuilder()
        .withData(updatedCurrency)
        .withStatusCode(HttpStatus.OK)
        .build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Response> deleteCurrency(@PathVariable Long id) {
    currencyService.deleteCurrency(id);
    return ApiResponseSerializer
        .successResponseSerializerBuilder()
        .withMessage("Currency deleted successfully")
        .withStatusCode(HttpStatus.NO_CONTENT)
        .build();
  }
}
