package com.vishvendra.procart.controller;

import com.vishvendra.procart.entities.Admin;
import com.vishvendra.procart.exception.AccessDeniedWrapperException;
import com.vishvendra.procart.exception.BadCredentialsException;
import com.vishvendra.procart.exception.ResourceNotFoundException;
import com.vishvendra.procart.filter.CustomUsernamePasswordAuthenticationToken;
import com.vishvendra.procart.repository.AdminRepository;
import com.vishvendra.procart.repository.UserRepository;
import com.vishvendra.procart.utils.JwtTokenUtil;
import com.vishvendra.procart.utils.response.ApiResponseSerializer;
import com.vishvendra.procart.utils.response.Response;
import com.vishvendra.procart.utils.securitymodel.CustomUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authenticate")
public class AuthenticationRestController {

  private final AuthenticationProvider customUsernamePasswordAuthenticationProvider;
  private final UserRepository userRepository;
  private final AdminRepository adminRepository;
  private final JwtTokenUtil jwtTokenUtil;
  @Value("${JWT_EXPIRATION_IN_MILLIS:3600000}")
  private Long jwtSecretExpirationInMillis;

  @PostMapping(produces = "application/json", consumes = "application/json")
  public ResponseEntity<Response> authenticate(
      @Valid @RequestBody AuthenticateRequest authenticateRequest) {
    log.info("Authenticating user: {}", authenticateRequest.username);
    final Authentication authentication = new CustomUsernamePasswordAuthenticationToken(
        authenticateRequest.username, authenticateRequest.password);
    final Authentication authenticationCheck;
    try {
      authenticationCheck = this.customUsernamePasswordAuthenticationProvider.authenticate(
          authentication);
    } catch (AuthenticationException e) {
      log.error(e.getMessage(), e);
      throw BadCredentialsException.create("Unauthorized access.",
          String.format("User unauthenticated: %s", authenticateRequest.username));
    }
    if (!authenticationCheck.isAuthenticated()) {
      throw BadCredentialsException.create("Unauthorized access.",
          String.format("User unauthenticated: %s", authenticateRequest.username));
    }
    log.info("User authenticated: {}", authenticateRequest.username);
    CustomUser principal = (CustomUser) authenticationCheck.getPrincipal();
    return handleUserAndAdminResponse(principal);
  }

  private String generateUserFullName(com.vishvendra.procart.entities.User user) {
    String fullName;
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

  private String generateAdminFullName(Admin admin) {
    String fullName;
    if (Objects.nonNull(admin.getProfileDetails())) {
      if (Objects.nonNull(admin.getProfileDetails().getFirstName())) {
        fullName = admin.getProfileDetails().getFirstName();
        if (Objects.nonNull(admin.getProfileDetails().getLastName())) {
          fullName = fullName.concat(" ").concat(admin.getProfileDetails().getLastName());
        }
      } else {
        fullName = admin.getUsername();
      }
    } else {
      fullName = admin.getUsername();
    }
    return fullName;
  }

  private ResponseEntity<Response> handleUserAndAdminResponse(CustomUser principal) {
    Optional<GrantedAuthority> authority = principal.getAuthorities().stream().findFirst();
    if (authority.isEmpty()) {
      throw AccessDeniedWrapperException.create("Unauthorized access.",
          String.format("User is not allowed to access this resource: %s",
              principal.getUsername()));
    }
    return switch (authority.get().getAuthority()) {
      case "ROLE_ADMIN" -> {
        Admin admin = this.adminRepository.findByUsername(principal.getUsername())
            .orElseThrow(() -> ResourceNotFoundException.create("Unauthorized.",
                String.format("User not found by username %s", principal.getUsername()),
                404));
        String adminFullName = generateAdminFullName(admin);
        yield ApiResponseSerializer.successResponseSerializerBuilder()
            .withStatusCode(HttpStatus.OK)
            .withData(Map.of(
                "token", jwtTokenUtil.generateToken(principal.getUsername())
                , "x-expiry-token", getExpiryTimeForToken()))
            .withMessage(
                String.format("Welcome! %s.", adminFullName)
            )
            .build();
      }
      case "ROLE_USER" -> {
        com.vishvendra.procart.entities.User user = this.userRepository.findByUsername(
                principal.getUsername())
            .orElseThrow(() -> ResourceNotFoundException.create("Unauthorized.",
                String.format("User not found by username %s", principal.getUsername()),
                404));
        String token = jwtTokenUtil.generateToken(user.getUsername());
        yield ApiResponseSerializer.successResponseSerializerBuilder()
            .withStatusCode(HttpStatus.OK)
            .withData(Map.of("token", token, "x-expiry-token", getExpiryTimeForToken()))
            .withMessage(
                String.format("Welcome! %s.", generateUserFullName(user)))
            .build();
      }
      default -> throw AccessDeniedWrapperException.create("Unauthorized access.",
          String.format("User is not allowed to access this resource: %s",
              principal.getUsername()));
    };
  }

  private String getExpiryTimeForToken() {
    LocalDateTime now = LocalDateTime.now();
    Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
    Instant updatedInstant = instant.plusMillis(jwtSecretExpirationInMillis);
    LocalDateTime updatedDateTime = LocalDateTime.ofInstant(updatedInstant, ZoneId.systemDefault());
    return updatedDateTime.toString();
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
