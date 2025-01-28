package com.vishvendra.procart.service.user;

import com.vishvendra.procart.entities.User;
import com.vishvendra.procart.exception.ResourceNotFoundException;
import com.vishvendra.procart.model.ProfileDetailsDTO;
import com.vishvendra.procart.model.UserAddress;
import com.vishvendra.procart.model.UserDTO;

public interface UserService {

  UserDTO createUser(UserDTO user);

  UserDTO get(Long id) throws ResourceNotFoundException;

  UserDTO retrieveByUsername(String username) throws ResourceNotFoundException;

  UserDTO updateUser(Long userId, ProfileDetailsDTO profileDetailsDTO)
      throws ResourceNotFoundException;

  UserAddress getUserAddress(final User user) throws ResourceNotFoundException;
}
