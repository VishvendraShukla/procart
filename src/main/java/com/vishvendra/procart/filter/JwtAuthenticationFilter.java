package com.vishvendra.procart.filter;

import com.vishvendra.procart.exception.BadCredentialsException;
import com.vishvendra.procart.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
      JwtTokenUtil jwtTokenUtil) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
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
    if (uri.matches("/api/v1/cart") || uri.matches("/api/v1/cart/complete")) {
      return false;
    }
    if (uri.matches("^/api/v1/user(/.*)?$")) {
      return request.getMethod().equalsIgnoreCase("POST");
    }
    return true;
  }
}