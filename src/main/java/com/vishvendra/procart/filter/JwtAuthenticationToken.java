package com.vishvendra.procart.filter;

import com.vishvendra.procart.utils.securitymodel.CustomUser;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken implements Authentication {

  @Getter
  private String token;

  private CustomUser customUser;

  public JwtAuthenticationToken(CustomUser customUser) {
    this.customUser = customUser;
  }

  public JwtAuthenticationToken(String token) {
    this.token = token;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return customUser.getAuthorities();
  }

  @Override
  public Object getCredentials() {
    return customUser.getPassword();
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return customUser;
  }

  @Override
  public boolean isAuthenticated() {
    return true;
  }

  @Override
  public void setAuthenticated(boolean b) throws IllegalArgumentException {

  }

  @Override
  public String getName() {
    return customUser.getUsername();
  }

}