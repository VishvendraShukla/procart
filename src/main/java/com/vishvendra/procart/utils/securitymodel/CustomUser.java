package com.vishvendra.procart.utils.securitymodel;

import com.vishvendra.procart.entities.Admin;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class CustomUser extends org.springframework.security.core.userdetails.User {

  private final Long userId;

  public CustomUser(Admin admin, List<GrantedAuthority> authorities) {
    super(admin.getUsername(),
        admin.getPassword(),
        !admin.isDeleted(),
        !admin.isDeleted(),
        true,
        !admin.isDeleted(),
        authorities);
    this.userId = admin.getId();
  }

  public CustomUser(com.vishvendra.procart.entities.User user, List<GrantedAuthority> authorities) {
    super(user.getUsername(),
        user.getPassword(),
        !user.isDeleted(),
        !user.isDeleted(),
        true,
        !user.isDeleted(),
        authorities);
    this.userId = user.getId();
  }

}
