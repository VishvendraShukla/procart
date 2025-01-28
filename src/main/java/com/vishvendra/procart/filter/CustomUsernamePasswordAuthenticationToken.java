package com.vishvendra.procart.filter;

import com.vishvendra.procart.utils.securitymodel.CustomUser;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class CustomUsernamePasswordAuthenticationToken implements Authentication {

  @Getter
  private String username;

  @Getter
  private String password;

  private CustomUser customUser;

  public CustomUsernamePasswordAuthenticationToken(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public CustomUsernamePasswordAuthenticationToken(CustomUser customUser) {
    this.customUser = customUser;
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
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

  }

  @Override
  public String getName() {
    return customUser.getUsername();
  }
}
