package com.vishvendra.procart.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends GlobalApplicationException {

  private ResourceNotFoundException(String displayMessage, String errorMessageForLogging,
      int statusCode) {
    super(displayMessage, errorMessageForLogging, statusCode);
  }

  private ResourceNotFoundException(String displayMessage, Throwable cause,
      String errorMessageForLogging, int statusCode) {
    super(displayMessage, cause, errorMessageForLogging, statusCode);
  }

  public static ResourceNotFoundException create(String displayMessage, String loggingMessage) {
    return new ResourceNotFoundException(displayMessage, loggingMessage, 404);
  }

  public static ResourceNotFoundException create(String displayMessage,
      String loggingMessage, Integer statusCode) {
    return new ResourceNotFoundException(displayMessage, loggingMessage, statusCode);
  }

}


