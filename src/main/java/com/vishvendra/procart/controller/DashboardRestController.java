package com.vishvendra.procart.controller;

import com.vishvendra.procart.service.dashboard.DashboardService;
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
@RequestMapping("api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardRestController {

  private final DashboardService dashboardService;

  @GetMapping
  public ResponseEntity<Response> retrieveDashboardData() {
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withData(dashboardService.getDashboardCardData())
        .build();
  }

  @GetMapping(value = "users")
  public ResponseEntity<Response> retrieveDashboardDataOfUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withData(dashboardService.getDashboardUserData(PageRequest.of(page, size)))
        .build();
  }

}
