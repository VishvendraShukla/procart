package com.vishvendra.procart.utils.startup;

import com.vishvendra.procart.model.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserGenerator {

  public static UserDTO createRandomUser() {
    String UUID = java.util.UUID.randomUUID().toString();
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("user-" + UUID);
    userDTO.setPassword(UUID);
    return userDTO;
  }

}
