package com.vishvendra.procart.filter;

import com.vishvendra.procart.exception.BadCredentialsException;
import com.vishvendra.procart.service.CustomUserDetailsService;
import com.vishvendra.procart.utils.JwtTokenUtil;
import com.vishvendra.procart.utils.securitymodel.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service("jwtAuthenticationProvider")
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final JwtTokenUtil jwtTokenUtil;
  private final CustomUserDetailsService customUserDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String token = "";
    if (authentication instanceof JwtAuthenticationToken) {
      token = ((JwtAuthenticationToken) authentication).getToken();
    }
    if (!token.isEmpty() && jwtTokenUtil.validateJwtToken(token)) {
      String username = jwtTokenUtil.getUsernameFromJwtToken(token);
      CustomUser user = (CustomUser) customUserDetailsService.loadUserByUsername(username);
      return new JwtAuthenticationToken(user);
    }
    throw BadCredentialsException.create("Unauthorized access.", "Bad Credentials");
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return JwtAuthenticationToken.class.equals(aClass);
  }

}
