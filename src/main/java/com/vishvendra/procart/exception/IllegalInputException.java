package com.vishvendra.procart.exception;

import lombok.Getter;

@Getter
public class IllegalInputException extends GlobalApplicationException {

  public IllegalInputException(String displayMessage, String errorMessageForLogging,
      Integer statusCode) {
    super(displayMessage, errorMessageForLogging, statusCode);
  }

  public static IllegalInputException create(String displayMessage, String loggingMessage) {
    return new IllegalInputException(displayMessage, loggingMessage, 400);
  }

}
