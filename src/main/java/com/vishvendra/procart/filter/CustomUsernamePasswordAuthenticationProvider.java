package com.vishvendra.procart.filter;

import com.vishvendra.procart.exception.BadCredentialsException;
import com.vishvendra.procart.service.CustomUserDetailsService;
import com.vishvendra.procart.utils.securitymodel.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("customUsernamePasswordAuthenticationProvider")
@RequiredArgsConstructor
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

  private final CustomUserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    if (authentication instanceof CustomUsernamePasswordAuthenticationToken) {
      String username = ((CustomUsernamePasswordAuthenticationToken) authentication).getUsername();
      String password = ((CustomUsernamePasswordAuthenticationToken) authentication).getPassword();
      CustomUser customUser = userDetailsService.loadUserByUsername(username);
      if (!this.passwordEncoder.matches(password,
          userDetailsService.loadUserByUsername(username).getPassword())) {
        throw BadCredentialsException.create("Unauthorized access.", "Bad Credentials");
      }
      return new CustomUsernamePasswordAuthenticationToken(customUser);
    }
    throw BadCredentialsException.create("Unauthorized access.", "Bad Credentials");
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return CustomUsernamePasswordAuthenticationToken.class.equals(authentication);
  }
}
