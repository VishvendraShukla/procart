package com.vishvendra.procart.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

  private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
  private final ObjectMapper objectMapper = new ObjectMapper();

  public LoggingFilter() {
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {
    String correlationId = request.getHeader(CORRELATION_ID_HEADER);
    if (correlationId == null) {
      correlationId = UUID.randomUUID().toString();
    }
    MDC.put(CORRELATION_ID_HEADER, correlationId);
    CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);
    Instant start = Instant.now();
    try {
      logRequestBefore(wrappedRequest);
      filterChain.doFilter(wrappedRequest, response);
      logRequestAfter(start, Instant.now());
    } finally {
      MDC.clear();
    }
  }

  private void logRequestBefore(CachedBodyHttpServletRequest request) {
    try {
      String requestURI = request.getRequestURI();
      Map<String, Object> logDetails = new HashMap<>();
      logDetails.put("uri", requestURI);
      logDetails.put("method", request.getMethod());
      logDetails.put("requestParams", request.getQueryString());
      logDetails.put("remoteAddress", request.getRemoteAddr());
      logDetails.put("userAgent", request.getHeader("User-Agent"));
      if (requestURI.matches("/api/v1/(authenticate)")) {
        logDetails.put("requestBody", "Emitting request body logging for security purpose.");
      } else {
        logDetails.put("requestBody", new String(request.getCachedBody()));
      }
      logDetails.put("timestamp", Instant.now().toString());
      String logJson = objectMapper.writeValueAsString(logDetails);
      log.info(logJson);
    } catch (Exception e) {
      log.error("Error logging request: {}", e.getMessage());
    }
  }

  private void logRequestAfter(Instant start, Instant end) {
    try {
      Map<String, Object> logDetails = new HashMap<>();
      logDetails.put("responseTimeMs", end.toEpochMilli() - start.toEpochMilli());
      String logJson = objectMapper.writeValueAsString(logDetails);
      log.info(logJson);
    } catch (Exception e) {
      log.error("Error logging request: {}", e.getMessage());
    }
  }
}
