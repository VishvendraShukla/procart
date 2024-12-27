package com.vishvendra.procart.utils.response;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseSerializer {

  private ApiResponseSerializer() {
  }

  public static SuccessResponseBuilder successResponseSerializerBuilder() {
    return new SuccessResponseBuilder();
  }

  public static ErrorResponseBuilder errorResponseSerializerBuilder() {
    return new ErrorResponseBuilder();
  }

  public static class SuccessResponseBuilder {

    private String message = "Operation successful";
    private Object data;
    private int statusCode = HttpStatus.OK.value();
    private String timestamp = LocalDateTime.now().toString();

    public SuccessResponseBuilder withMessage(String message) {
      this.message = message;
      return this;
    }

    public SuccessResponseBuilder withData(Object data) {
      this.data = data;
      return this;
    }

    public SuccessResponseBuilder withStatusCode(HttpStatus status) {
      this.statusCode = status.value();
      return this;
    }

    public SuccessResponseBuilder withTimestamp(String timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public ResponseEntity<Response> build() {
      Response response = new Response(
          true,
          message,
          data,
          statusCode,
          timestamp
      );
      return new ResponseEntity<>(response, HttpStatus.valueOf(statusCode));
    }
  }

  public static class ErrorResponseBuilder {

    private String message = "An error occurred";
    private Object data;
    private int statusCode = HttpStatus.BAD_REQUEST.value();
    private String timestamp = LocalDateTime.now().toString();

    public ErrorResponseBuilder withMessage(String message) {
      this.message = message;
      return this;
    }

    public ErrorResponseBuilder withData(Object data) {
      this.data = data;
      return this;
    }

    public ErrorResponseBuilder withStatusCode(HttpStatus status) {
      this.statusCode = status.value();
      return this;
    }

    public ErrorResponseBuilder withTimestamp(String timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    // Method to build the final ResponseEntity
    public ResponseEntity<Response> build() {
      Response response = new Response(
          false,
          message,
          data,
          statusCode,
          timestamp
      );
      return new ResponseEntity<>(response, HttpStatus.valueOf(statusCode));
    }
  }
}
