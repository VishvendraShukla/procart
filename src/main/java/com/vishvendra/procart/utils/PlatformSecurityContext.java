package com.vishvendra.procart.utils;

import com.vishvendra.procart.utils.securitymodel.CustomUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class PlatformSecurityContext {

  public static CustomUser getLoggedUser() {
    return (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

}
