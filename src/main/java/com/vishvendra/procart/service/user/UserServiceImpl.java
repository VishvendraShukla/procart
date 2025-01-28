package com.vishvendra.procart.service.user;

import com.vishvendra.procart.entities.ProfileDetails;
import com.vishvendra.procart.entities.User;
import com.vishvendra.procart.exception.ResourceNotFoundException;
import com.vishvendra.procart.mapper.UserMapper;
import com.vishvendra.procart.model.ProfileDetailsDTO;
import com.vishvendra.procart.model.UserAddress;
import com.vishvendra.procart.model.UserDTO;
import com.vishvendra.procart.repository.ProfileDetailsRepository;
import com.vishvendra.procart.repository.UserRepository;
import com.vishvendra.procart.utils.hash.HashService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final ProfileDetailsRepository profileDetailsRepository;
  private final HashService hashService;

  @Override
  @Transactional
  public UserDTO createUser(UserDTO userDTO) {
    User user = this.userMapper.toEntity(userDTO);
    user.setPassword(this.hashService.encodePassword(userDTO.getPassword()));
    if (userDTO.getProfileDetailsDTO() != null) {
      ProfileDetails profileDetails = this.profileDetailsRepository.save(
          this.userMapper.toEntity(userDTO.getProfileDetailsDTO()));
      user.setProfileDetails(profileDetails);
    }
    this.userRepository.save(user);
    UserDTO response = this.userMapper.toDTO(user);
    response.setPassword(null);
    return response;
  }

  @Override
  public UserDTO get(Long id) throws ResourceNotFoundException {
    User userToReturn = this.userRepository.findById(id)
        .orElseThrow(() -> ResourceNotFoundException
            .create("Please check inputted data and try again",
                String.format("User not found by id %s", id)));
    UserDTO response = this.userMapper.toDTO(userToReturn);
    response.setPassword(null);
    return response;
  }

  @Override
  public UserDTO retrieveByUsername(String username) throws ResourceNotFoundException {
    User userToReturn = this.userRepository.findByUsername(username)
        .orElseThrow(() -> ResourceNotFoundException
            .create("Please check inputted data and try again",
                String.format("User not found by username %s", username)));
    UserDTO response = this.userMapper.toDTO(userToReturn);
    response.setPassword(null);
    return response;
  }

  @Override
  @Transactional
  public UserDTO updateUser(Long userId, ProfileDetailsDTO profileDetailsDTO)
      throws ResourceNotFoundException {
    User existingUser = this.userRepository.findById(userId).orElseThrow(()
        -> ResourceNotFoundException.create("Please check inputted data and try again",
        String.format("User not found by id %s", userId)));

    User updatedUser = this.userMapper.mergeProfileData(existingUser, profileDetailsDTO);
    ProfileDetails profileDetails = this.profileDetailsRepository.save(
        updatedUser.getProfileDetails());
    updatedUser.setProfileDetails(profileDetails);

    this.userRepository.save(updatedUser);
    UserDTO response = this.userMapper.toDTO(updatedUser);
    response.setPassword(null);
    return response;
  }

  @Override
  public UserAddress getUserAddress(User user) throws ResourceNotFoundException {
    if (Objects.isNull(user.getProfileDetails())) {
      throw ResourceNotFoundException.create("Please check inputted data and try again",
          String.format("User Details for address not found by id %s", user.getId()));
    }
    ProfileDetails profileDetails = user.getProfileDetails();
    UserAddress.UserAddressBuilder userAddressBuilder = UserAddress.builder();
    if (Objects.nonNull(profileDetails.getAddress())) {
      userAddressBuilder.address(profileDetails.getAddress());
    }
    if (Objects.nonNull(profileDetails.getCity())) {
      userAddressBuilder.city(profileDetails.getCity());
    }
    if (Objects.nonNull(profileDetails.getCountry())) {
      userAddressBuilder.country(profileDetails.getCountry());
    }
    if (Objects.nonNull(profileDetails.getState())) {
      userAddressBuilder.state(profileDetails.getState());
    }
    if (Objects.nonNull(profileDetails.getPinCode())) {
      userAddressBuilder.pinCode(profileDetails.getPinCode());
    }
    return userAddressBuilder.build();
  }

}
