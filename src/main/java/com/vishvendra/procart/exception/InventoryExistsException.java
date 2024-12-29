package com.vishvendra.procart.exception;

import lombok.Getter;

@Getter
public class InventoryExistsException extends GlobalApplicationException {

  public InventoryExistsException(String displayMessage, String errorMessageForLogging,
      Integer statusCode) {
    super(displayMessage, errorMessageForLogging, statusCode);
  }

  public InventoryExistsException(String displayMessage, Throwable cause,
      String errorMessageForLogging, Integer statusCode) {
    super(displayMessage, cause, errorMessageForLogging, statusCode);
  }

  public static InventoryExistsException create(String displayMessage,
      String errorMessageForLogging) {
    return new InventoryExistsException(displayMessage, errorMessageForLogging, 409);
  }
}
