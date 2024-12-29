package com.vishvendra.procart.exception;

import lombok.Getter;

@Getter
public class InsufficientStockException extends GlobalApplicationException {

  private InsufficientStockException(String displayMessage, String errorMessageForLogging,
      int statusCode) {
    super(displayMessage, errorMessageForLogging, statusCode);
  }

  private InsufficientStockException(String displayMessage, Throwable cause,
      String errorMessageForLogging, int statusCode) {
    super(displayMessage, cause, errorMessageForLogging, statusCode);
  }

  public static InsufficientStockException create(String displayMessage, String loggingMessage) {
    return new InsufficientStockException(displayMessage, loggingMessage,
        400);
  }

  public static InsufficientStockException create(String displayMessage, String loggingMessage,
      Integer statusCode) {
    return new InsufficientStockException(displayMessage, loggingMessage, statusCode);
  }

}

