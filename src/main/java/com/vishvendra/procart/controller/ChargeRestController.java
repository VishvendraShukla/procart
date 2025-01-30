package com.vishvendra.procart.controller;

import com.vishvendra.procart.entities.ChargeAppliesOn;
import com.vishvendra.procart.model.ChargeDTO;
import com.vishvendra.procart.model.PageResultResponse;
import com.vishvendra.procart.service.charge.ChargeService;
import com.vishvendra.procart.utils.response.ApiResponseSerializer;
import com.vishvendra.procart.utils.response.Response;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/charge")
@RequiredArgsConstructor
public class ChargeRestController {

  private final ChargeService chargeService;

  @PostMapping
  public ResponseEntity<Response> createCharge(@Valid @RequestBody ChargeDTO chargeDTO) {
    ChargeDTO createdCharge = chargeService.create(chargeDTO);
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withMessage("Charge created successfully")
        .withData(createdCharge)
        .withStatusCode(HttpStatus.CREATED)
        .build();
  }

  @GetMapping
  public ResponseEntity<Response> getChargeByFilters(
      @RequestParam(required = false) Long id,
      @RequestParam(required = false) String description,
      @RequestParam(required = false) String displayName,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    PageResultResponse<ChargeDTO> chargeByFilters = chargeService.getWithFilters(id, description,
        displayName, PageRequest.of(page, size));
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withData(chargeByFilters)
        .withStatusCode(HttpStatus.OK)
        .build();
  }

  @GetMapping(value = "/{appliesOn}")
  public ResponseEntity<Response> getChargeByAppliedOn(@PathVariable String appliesOn) {
    List<ChargeDTO> chargesByAppliesOn = this.chargeService.findChargesByAppliesOn(
        ChargeAppliesOn.valueOf(appliesOn));
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withData(chargesByAppliesOn)
        .withStatusCode(HttpStatus.OK)
        .build();
  }


}
