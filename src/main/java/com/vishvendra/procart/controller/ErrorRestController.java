package com.vishvendra.procart.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishvendra.procart.exception.GlobalApplicationException;
import com.vishvendra.procart.utils.response.ApiResponseSerializer;
import com.vishvendra.procart.utils.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ErrorRestController implements ErrorController {

  private final ErrorAttributes errorAttributes;
  private final ObjectMapper objectMapper;

  @RequestMapping("/error")
  public ResponseEntity<Response> handleError(HttpServletRequest request) {
    Map<String, Object> errorDetails = getErrorAttributes(request);
    HttpStatus status = getStatus(request);
    JsonNode errorData = null;
    try {
      String errorDetailsJson = checkIfErrorIsFromFilterAndNeedsToHaveDifferentResponse(
          errorDetails);
      if (Objects.isNull(errorDetailsJson)) {
        errorDetailsJson = objectMapper.writeValueAsString(errorDetails);
        errorData = objectMapper.readTree(errorDetailsJson);
        log.error("Error around filter: {}", errorData.toPrettyString());
      } else {
        log.error("Error occurred: {}", errorDetailsJson);
      }
    } catch (Exception e) {
      log.error("Error occurred, but failed to convert error details to JSON: {}", errorDetails, e);
    }
    return ApiResponseSerializer.errorResponseSerializerBuilder()
        .withStatusCode(status)
        .withMessage("An error occurred while processing the request.")
        .build();
  }

  private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
    ErrorAttributeOptions options = ErrorAttributeOptions.of(
        Include.MESSAGE,
        Include.EXCEPTION
    );
    WebRequest webRequest = new ServletWebRequest(request);
    Map<String, Object> errorAttributesMap = errorAttributes.getErrorAttributes(webRequest,
        options);

    Throwable error = errorAttributes.getError(webRequest);
    if (error instanceof GlobalApplicationException exception) {
      errorAttributesMap.put("displayMessage", exception.getDisplayMessage());
      errorAttributesMap.put("errorMessageForLogging", exception.getErrorMessageForLogging());
      errorAttributesMap.put("statusCode", exception.getStatusCode());
    }

    return errorAttributesMap;
  }


  private String checkIfErrorIsFromFilterAndNeedsToHaveDifferentResponse(
      Map<String, Object> errorDetails) {
    if (errorDetails.containsKey("exception")) {
      String exceptionType = errorDetails.get("exception").toString();
      if (exceptionType.contains("ResourceNotFoundException")) {
        return "An unexpected error occurred. Please try again later.";
      }
    }
    return null;
  }

  private HttpStatus getStatus(HttpServletRequest request) {
    Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
    if (statusCode != null) {
      try {
        return HttpStatus.valueOf(statusCode);
      } catch (Exception ex) {
        return HttpStatus.INTERNAL_SERVER_ERROR;
      }
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }


}

