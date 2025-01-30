package com.vishvendra.procart.exception;

import lombok.Getter;

@Getter
public class OrderException extends GlobalApplicationException {

  private OrderException(String displayMessage, String errorMessageForLogging, Integer statusCode) {
    super(displayMessage, errorMessageForLogging, statusCode);
  }

  private OrderException(String displayMessage, Throwable cause, String errorMessageForLogging,
      Integer statusCode) {
    super(displayMessage, cause, errorMessageForLogging, statusCode);
  }

  public static OrderException create(String displayMessage, String loggingMessage) {
    return new OrderException(displayMessage, loggingMessage, 400);
  }
}
