package com.vishvendra.procart.exception;

import lombok.Getter;

@Getter
public abstract class GlobalApplicationException extends RuntimeException {

  private final String displayMessage;
  private final String errorMessageForLogging;
  private final Integer statusCode;

  public GlobalApplicationException(String displayMessage, String errorMessageForLogging,
      Integer statusCode) {
    super(errorMessageForLogging);
    this.displayMessage = displayMessage;
    this.errorMessageForLogging = errorMessageForLogging;
    this.statusCode = statusCode;
  }

  public GlobalApplicationException(String displayMessage, Throwable cause,
      String errorMessageForLogging, Integer statusCode) {
    super(errorMessageForLogging, cause);
    this.displayMessage = displayMessage;
    this.errorMessageForLogging = errorMessageForLogging;
    this.statusCode = statusCode;
  }

}

