package com.vishvendra.procart.exception;

public class BadCredentialsException extends GlobalApplicationException {

  public BadCredentialsException(String displayMessage, String errorMessageForLogging,
      Integer statusCode) {
    super(displayMessage, errorMessageForLogging, statusCode);
  }

  public BadCredentialsException(String displayMessage, Throwable cause,
      String errorMessageForLogging, Integer statusCode) {
    super(displayMessage, cause, errorMessageForLogging, statusCode);
  }

  public static BadCredentialsException create(String displayMessage,
      String errorMessageForLogging) {
    return new BadCredentialsException(displayMessage, errorMessageForLogging, 401);
  }
}
