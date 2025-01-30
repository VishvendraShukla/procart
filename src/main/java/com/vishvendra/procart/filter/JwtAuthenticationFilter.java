package com.vishvendra.procart.filter;

import com.vishvendra.procart.exception.BadCredentialsException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final static String[] JWT_URLS = {
      "/api/v1/admin",
      "/api/v1/products",
      "/api/v1/inventories",
      "/api/v1/charge",
      "/api/v1/dashboard",
      "/api/v1/auditlogs",
      "/api/v1/currencies",
      "/api/v1/cart",
      "/api/v1/orders",
      "/api/v1/cart/complete"
  };

  private final AuthenticationManager authenticationManager;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {

    final String authorizationHeader = request.getHeader("Authorization");
    String jwt = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
    }

    if (jwt != null) {
      Authentication authentication = new JwtAuthenticationToken(jwt);
      authentication = this.authenticationManager.authenticate(authentication);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      chain.doFilter(request, response);
    } else {
      throw BadCredentialsException.create("Unauthorized", "Invalid or expired JWT token");
    }
  }


  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String uri = request.getRequestURI();
    if (Arrays.stream(JWT_URLS).anyMatch(uri::matches)) {
      return false;
    }
    if (uri.matches("^/api/v1/user(/.*)?$")) {
      return request.getMethod().equalsIgnoreCase("POST");
    }
    return true;
  }
}