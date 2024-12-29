package com.vishvendra.procart.exception;

import lombok.Getter;

@Getter
public class CartAlreadyActiveException extends GlobalApplicationException {

  private CartAlreadyActiveException(String displayMessage, String errorMessageForLogging,
      int statusCode) {
    super(displayMessage, errorMessageForLogging, statusCode);
  }

  private CartAlreadyActiveException(String displayMessage, Throwable cause,
      String errorMessageForLogging, int statusCode) {
    super(displayMessage, cause, errorMessageForLogging, statusCode);
  }

  public static CartAlreadyActiveException create(String displayMessage, String loggingMessage) {
    return new CartAlreadyActiveException(displayMessage, loggingMessage, 404);
  }

}
