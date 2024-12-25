package com.vishvendra.procart.mapper;

import com.vishvendra.procart.entities.ProfileDetails;
import com.vishvendra.procart.entities.User;
import com.vishvendra.procart.model.ProfileDetailsDTO;
import com.vishvendra.procart.model.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

  @Mapping(source = "profileDetailsDTO", target = "profileDetails")
  User toEntity(UserDTO adminDTO);

  @Mapping(source = "profileDetails", target = "profileDetailsDTO")
  UserDTO toDTO(User admin);

  @Mapping(target = "profileDetails", ignore = true)
  default User mergeUser(User existingUser, UserDTO userDTO) {
    if (userDTO.getUsername() != null) {
      existingUser.setUsername(userDTO.getUsername());
    }
    if (userDTO.getPassword() != null) {
      existingUser.setPassword(userDTO.getPassword());
    }
    if (userDTO.getProfileDetailsDTO() != null) {
      ProfileDetails details = toEntity(userDTO.getProfileDetailsDTO());
      existingUser.setProfileDetails(details);
    }
    return existingUser;
  }

  ProfileDetails toEntity(ProfileDetailsDTO profileDetailsDTO);

  ProfileDetailsDTO toDTO(ProfileDetails profileDetails);
}
