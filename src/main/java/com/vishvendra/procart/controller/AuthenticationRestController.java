package com.vishvendra.procart.controller;

import com.vishvendra.procart.exception.BadCredentialsException;
import com.vishvendra.procart.exception.ResourceNotFoundException;
import com.vishvendra.procart.repository.UserRepository;
import com.vishvendra.procart.utils.JwtTokenUtil;
import com.vishvendra.procart.utils.response.ApiResponseSerializer;
import com.vishvendra.procart.utils.response.Response;
import com.vishvendra.procart.utils.securitymodel.CustomUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authenticate")
public class AuthenticationRestController {

  private final DaoAuthenticationProvider customAuthenticationProvider;
  private final UserRepository userRepository;
  private final JwtTokenUtil jwtTokenUtil;

  @PostMapping(produces = "application/json", consumes = "application/json")
  public ResponseEntity<Response> authenticate(
      @Valid @RequestBody AuthenticateRequest authenticateRequest) {
    log.info("Authenticating user: {}", authenticateRequest.username);
    final Authentication authentication = new UsernamePasswordAuthenticationToken(
        authenticateRequest.username, authenticateRequest.password);
    final Authentication authenticationCheck;
    try {
      authenticationCheck = this.customAuthenticationProvider.authenticate(
          authentication);
    } catch (AuthenticationException e) {
      throw BadCredentialsException.create("Unauthorized access.",
          String.format("User unauthenticated: %s", authenticateRequest.username));
    }
    if (!authenticationCheck.isAuthenticated()) {
      throw BadCredentialsException.create("Unauthorized access.",
          String.format("User unauthenticated: %s", authenticateRequest.username));
    }
    log.info("User authenticated: {}", authenticateRequest.username);
    CustomUser principal = (CustomUser) authenticationCheck.getPrincipal();
    com.vishvendra.procart.entities.User user = this.userRepository.findByUsername(
            principal.getUsername())
        .orElseThrow(() -> ResourceNotFoundException.create("Unauthorized.",
            String.format("User not found by username %s", authenticateRequest.username),
            404));
    String token = jwtTokenUtil.generateToken(user.getUsername());
    return ApiResponseSerializer.successResponseSerializerBuilder()
        .withStatusCode(HttpStatus.OK)
        .withData(Map.of("token", token))
        .withMessage(
            String.format("Welcome! %s.", generateUserFullName(user)))
        .build();
  }

  private String generateUserFullName(com.vishvendra.procart.entities.User user) {
    String fullName = "";
    if (Objects.nonNull(user.getProfileDetails())) {
      if (Objects.nonNull(user.getProfileDetails().getFirstName())) {
        fullName = user.getProfileDetails().getFirstName();
        if (Objects.nonNull(user.getProfileDetails().getLastName())) {
          fullName = fullName.concat(" ").concat(user.getProfileDetails().getLastName());
        }
      } else {
        fullName = user.getUsername();
      }
    } else {
      fullName = user.getUsername();
    }
    return fullName;
  }

  public static class AuthenticateRequest {

    @NotNull
    @NotBlank
    public String username;
    @NotNull
    @NotBlank
    public String password;
  }

}
