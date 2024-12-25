package com.vishvendra.procart.controller;

import com.vishvendra.procart.model.UserDTO;
import com.vishvendra.procart.service.user.UserService;
import com.vishvendra.procart.utils.response.ApiResponseSerializer;
import com.vishvendra.procart.utils.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserRestController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<Response> getUserByUsername(
      @RequestParam("username") final String username) {
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withStatusCode(HttpStatus.FOUND)
        .withData(this.userService.retrieveByUsername(username)).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Response> getUserById(@PathVariable final Long id) {
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withStatusCode(HttpStatus.FOUND)
        .withData(this.userService.get(id)).build();
  }

  @PostMapping
  public ResponseEntity<Response> createUser(@Valid @RequestBody UserDTO userDTO) {
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withStatusCode(HttpStatus.CREATED)
        .withData(this.userService.createUser(userDTO)).build();
  }
}
