package com.vishvendra.procart.exception;

import lombok.Getter;

@Getter
public class AccessDeniedWrapperException extends GlobalApplicationException {

  public AccessDeniedWrapperException(String displayMessage, String errorMessageForLogging,
      Integer statusCode) {
    super(displayMessage, errorMessageForLogging, statusCode);
  }

  public AccessDeniedWrapperException(String displayMessage, Throwable cause,
      String errorMessageForLogging, Integer statusCode) {
    super(displayMessage, cause, errorMessageForLogging, statusCode);
  }

  public static AccessDeniedWrapperException create(String displayMessage, String loggingMessage) {
    return new AccessDeniedWrapperException(displayMessage, loggingMessage, 403);
  }

}
