package com.vishvendra.procart.exception;

import com.vishvendra.procart.utils.response.ApiResponseSerializer;
import com.vishvendra.procart.utils.response.ApiResponseSerializer.ErrorResponseBuilder;
import com.vishvendra.procart.utils.response.Response;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Response> handleBadCredentialsException(BadCredentialsException ex) {
    log.error("BadCredentialsException: {}", ex.getMessage());
    return ApiResponseSerializer.errorResponseSerializerBuilder()
        .withMessage("Username/password does not match, unauthorised access.")
        .build();
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Response> handleRuntimeExceptionException(RuntimeException ex) {
    log.error("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
    return ApiResponseSerializer.errorResponseSerializerBuilder()
        .withMessage("An unexpected error occurred. Please try again later.")
        .build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Response> handleValidationException(MethodArgumentNotValidException ex) {
    log.error("ValidationException: {}", ex.getMessage());
    return ApiResponseSerializer.errorResponseSerializerBuilder()
        .withMessage("Validation failed")
        .build();
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Response> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex) {
    log.error("MissingServletRequestParameterException: {}", ex.getMessage());
    return ApiResponseSerializer.errorResponseSerializerBuilder()
        .withMessage("Missing data in request.")
        .build();
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Response> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex) {
    log.error("HttpRequestMethodNotSupportedException: {}", ex.getMessage());
    return ApiResponseSerializer.errorResponseSerializerBuilder()
        .withMessage("Method Not Allowed")
        .build();
  }

  @ExceptionHandler(PSQLException.class)
  public ResponseEntity<Response> handlePSQLException(PSQLException ex) {
    log.error("PSQLException: {}", ex.getMessage());
    return ApiResponseSerializer.errorResponseSerializerBuilder()
        .withMessage("Some error occurred.")
        .build();
  }

  @ExceptionHandler(GlobalApplicationException.class)
  public ResponseEntity<Response> handleGlobalApplicationException(GlobalApplicationException ex) {
    log.error("{}: {}", ex.getClass().getSimpleName(), ex.getErrorMessageForLogging(), ex);
    String displayMessage = ExceptionMessageFactory.getDefaultMessage(ex.getClass());
    if (!ex.getDisplayMessage().isEmpty()) {
      displayMessage = ex.getDisplayMessage();
    }
    ErrorResponseBuilder errorResponseBuilder = ApiResponseSerializer.errorResponseSerializerBuilder();
    errorResponseBuilder.withMessage(displayMessage);
    HttpStatus status;
    if (Objects.nonNull(ex.getStatusCode())) {
      status = HttpStatus.resolve(ex.getStatusCode());
      assert status != null;
    } else {
      status = HttpStatus.BAD_REQUEST;
    }
    errorResponseBuilder.withStatusCode(status);
    return errorResponseBuilder.build();
  }

}
