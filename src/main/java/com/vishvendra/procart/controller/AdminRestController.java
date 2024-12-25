package com.vishvendra.procart.controller;

import com.vishvendra.procart.model.AdminDTO;
import com.vishvendra.procart.model.ProfileDetailsDTO;
import com.vishvendra.procart.service.AdminService;
import com.vishvendra.procart.utils.response.ApiResponseSerializer;
import com.vishvendra.procart.utils.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminRestController {

  private final AdminService adminService;

  @GetMapping
  public ResponseEntity<Response> getAdminByUsername(
      @RequestParam("username") final String username) {
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withStatusCode(HttpStatus.FOUND)
        .withData(this.adminService.retrieveByUsername(username)).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Response> getAdmin(@PathVariable final Long id) {
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withStatusCode(HttpStatus.FOUND)
        .withData(this.adminService.get(id)).build();
  }

  @PostMapping
  public ResponseEntity<Response> createAdmin(@Valid @RequestBody AdminDTO adminDTO) {
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withStatusCode(HttpStatus.CREATED)
        .withData(this.adminService.createAdmin(adminDTO)).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Response> updateAdmin(@PathVariable final Long id,
      @Valid @RequestBody ProfileDetailsDTO profileDetailsDTO) {
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withStatusCode(HttpStatus.OK)
        .withData(this.adminService.updateAdmin(id, profileDetailsDTO)).build();
  }

}
