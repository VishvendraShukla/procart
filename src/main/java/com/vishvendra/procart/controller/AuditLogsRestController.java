package com.vishvendra.procart.controller;

import com.vishvendra.procart.service.audit.AuditService;
import com.vishvendra.procart.utils.response.ApiResponseSerializer;
import com.vishvendra.procart.utils.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auditlogs")
@RequiredArgsConstructor
public class AuditLogsRestController {

  private final AuditService auditService;

  @GetMapping
  public ResponseEntity<Response> retrieveAuditLogs(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withData(auditService.getAuditLogs(PageRequest.of(page, size)))
        .build();
  }

}
