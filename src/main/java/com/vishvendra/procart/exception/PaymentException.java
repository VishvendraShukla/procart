package com.vishvendra.procart.exception;

import lombok.Getter;

@Getter
public class PaymentException extends GlobalApplicationException {

  private PaymentException(String displayMessage, String errorMessageForLogging,
      Integer statusCode) {
    super(displayMessage, errorMessageForLogging, statusCode);
  }

  private PaymentException(String displayMessage, Throwable cause, String errorMessageForLogging,
      Integer statusCode) {
    super(displayMessage, cause, errorMessageForLogging, statusCode);
  }

  public static PaymentException create(String displayMessage, String loggingMessage) {
    return new PaymentException(displayMessage, loggingMessage, 500);
  }
}
