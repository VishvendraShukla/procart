package com.vishvendra.procart.controller;


import com.vishvendra.procart.model.ConfigurationDTO;
import com.vishvendra.procart.service.configuration.ConfigurationService;
import com.vishvendra.procart.utils.response.ApiResponseSerializer;
import com.vishvendra.procart.utils.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/configuration")
public class ConfigurationRestController {

  private final ConfigurationService configurationService;

  @PostMapping
  public ResponseEntity<Response> createConfiguration(
      @RequestBody ConfigurationDTO configurationDTO) {
    configurationService.create(configurationDTO);
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withMessage("Configuration created successfully")
        .build();
  }

  @PutMapping
  public ResponseEntity<Response> updateConfiguration(
      @RequestBody ConfigurationDTO configurationDTO) {
    configurationService.update(configurationDTO);
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withMessage("Configuration updated successfully")
        .build();
  }

  @GetMapping
  public ResponseEntity<Response> getConfiguration(
      @RequestParam(required = false) String key,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withData(configurationService.getConfiguration(key, PageRequest.of(page, size)))
        .build();
  }
}
