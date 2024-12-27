package com.vishvendra.procart.filter;

import com.vishvendra.procart.exception.BadCredentialsException;
import com.vishvendra.procart.utils.JwtTokenUtil;
import com.vishvendra.procart.utils.securitymodel.CustomUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final UserDetailsService userDetailsService;
  private final JwtTokenUtil jwtTokenUtil;
  private final AuthenticationManager authenticationManager;

  public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil,
      AuthenticationManager authenticationManager) {
    this.userDetailsService = userDetailsService;
    this.jwtTokenUtil = jwtTokenUtil;
    this.authenticationManager = authenticationManager;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {

//    String requestURI = request.getRequestURI();
//    if (!requestURI.startsWith("/api/v1/user")) {
//      chain.doFilter(request, response);
//      return;
//    }

    final String authorizationHeader = request.getHeader("Authorization");
    String jwt = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
    }

    if (jwt != null && jwtTokenUtil.validateJwtToken(jwt)) {
      CustomUser customUser = (CustomUser) userDetailsService.loadUserByUsername(
          jwtTokenUtil.getUsernameFromJwtToken(jwt));
      UsernamePasswordAuthenticationToken authRequest =
          UsernamePasswordAuthenticationToken.unauthenticated(customUser.getUsername(),
              customUser.getPassword());
      Authentication authentication = authenticationManager.authenticate(authRequest);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      chain.doFilter(request, response);
    } else {
      throw BadCredentialsException.create("Unauthorized", "Invalid or expired JWT token");
    }
  }


  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String uri = request.getRequestURI();
    return !uri.matches("^/api/v1/user(/.*)?$");
  }
}