package com.vishvendra.procart.permission;

import com.vishvendra.procart.exception.AccessDeniedWrapperException;
import com.vishvendra.procart.utils.securitymodel.CustomUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class RequiresOwnershipAspect {

  @Before(value = "@annotation(requiresOwnership) && args(..)", argNames = "joinPoint,requiresOwnership")
  public void enforceOwnership(JoinPoint joinPoint, RequiresOwnership requiresOwnership) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication.getPrincipal() instanceof CustomUser customUser)) {
      throw AccessDeniedWrapperException.create("Auth principle not an instance of CustomUser",
          "Access Denied");
    }
    boolean isAdmin = customUser.getAuthorities().stream()
        .anyMatch(
            grantedAuthority -> grantedAuthority.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
    if (isAdmin) {
      String logMessage = String.format("Admin user: %s, skipping ownership check",
          customUser.getUsername());
      log.info(logMessage);
      return;
    }
    String url = "N/A";
    String method = "N/A";
    String logMessage = "User with Username: {}, {} permission for Request URL: {}, HTTP Method: {}";
    String permission = "Denied";
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();
      url = request.getRequestURI();
      method = request.getMethod();
    }
    String action = requiresOwnership.action();
    String param = requiresOwnership.param();

    try {
      if ("VIEW".equalsIgnoreCase(action)) {
        checkOwnership(customUser, joinPoint, param, "VIEW");
      } else if ("UPDATE".equalsIgnoreCase(action)) {
        checkOwnership(customUser, joinPoint, param, "UPDATE");
      } else {
        throw new IllegalArgumentException("Unknown action: " + action);
      }
      permission = "Granted";
    } catch (Exception e) {
      throw e;
    } finally {
      log.info(logMessage, customUser.getUsername(), permission, url, method);
    }
  }

  private void checkOwnership(CustomUser user, JoinPoint joinPoint, String param, String action) {
    Object targetId = getIdFromArguments(joinPoint, param);

    if (targetId == null) {
      throw AccessDeniedWrapperException.create("You do not have permission to view this resource",
          "Target ID found null");
    }
    if ("VIEW".equalsIgnoreCase(action)) {
      if ((param.equalsIgnoreCase("id") && !targetId.equals(user.getUserId())) ||
          (param.equalsIgnoreCase("username") && !targetId.equals(user.getUsername()))) {
        throw AccessDeniedWrapperException.create(
            "You do not have permission to view this resource",
            "Either ID or Username not matched with user's target");
      }
    } else if ("UPDATE".equalsIgnoreCase(action)) {
      if ((!targetId.equals(user.getUserId()) && !targetId.equals(
          user.getUsername()))) {
        throw AccessDeniedWrapperException.create(
            "You do not have permission to view this resource",
            "Either ID or Username not matched with user's target");
      }
    }

  }

  private Object getIdFromArguments(JoinPoint joinPoint, String paramName) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    String[] parameterNames = signature.getParameterNames();
    Object[] arguments = joinPoint.getArgs();

    for (int i = 0; i < parameterNames.length; i++) {
      if (paramName.equals(parameterNames[i])) {
        return arguments[i];
      }
    }
    return null;
  }

}
