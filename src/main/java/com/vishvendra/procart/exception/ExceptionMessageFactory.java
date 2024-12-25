package com.vishvendra.procart.exception;

import java.util.HashMap;
import java.util.Map;

public class ExceptionMessageFactory {

  private static final Map<Class<? extends GlobalApplicationException>, String> messageMap = new HashMap<>();

  static {
    messageMap.put(ResourceNotFoundException.class, "The requested resource could not be found.");
  }

  public static String getDefaultMessage(
      Class<? extends GlobalApplicationException> exceptionClass) {
    return messageMap.getOrDefault(exceptionClass, "An unexpected error occurred.");
  }
}
