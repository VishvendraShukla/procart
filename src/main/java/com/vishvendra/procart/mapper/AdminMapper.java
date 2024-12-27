package com.vishvendra.procart.mapper;

import com.vishvendra.procart.entities.Admin;
import com.vishvendra.procart.entities.ProfileDetails;
import com.vishvendra.procart.model.AdminDTO;
import com.vishvendra.procart.model.ProfileDetailsDTO;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdminMapper {

  @Mapping(source = "profileDetailsDTO", target = "profileDetails")
  Admin toEntity(AdminDTO adminDTO);

  @Mapping(source = "profileDetails", target = "profileDetailsDTO")
  AdminDTO toDTO(Admin admin);

  @Mapping(target = "profileDetails", ignore = true)
  default Admin mergeAdmin(Admin existingAdmin, AdminDTO adminDTO) {
    if (adminDTO.getName() != null) {
      existingAdmin.setName(adminDTO.getName());
    }
    if (adminDTO.getUsername() != null) {
      existingAdmin.setUsername(adminDTO.getUsername());
    }
    if (adminDTO.getPassword() != null) {
      existingAdmin.setPassword(adminDTO.getPassword());
    }
    if (adminDTO.getProfileDetailsDTO() != null) {
      ProfileDetails details = toEntity(adminDTO.getProfileDetailsDTO());
      existingAdmin.setProfileDetails(details);
    }
    return existingAdmin;
  }

  @Mapping(target = "profileDetails", ignore = true)
  default Admin mergeProfileData(Admin existingAdmin, ProfileDetailsDTO profileDetailsDTO) {
    if (Objects.nonNull(profileDetailsDTO)) {
      ProfileDetails details = toEntity(profileDetailsDTO);
      if (Objects.nonNull(profileDetailsDTO.getId())) {
        details.setId(profileDetailsDTO.getId());
      }
      existingAdmin.setProfileDetails(details);
    }
    return existingAdmin;
  }

  ProfileDetails toEntity(ProfileDetailsDTO profileDetailsDTO);

  ProfileDetailsDTO toDTO(ProfileDetails profileDetails);
}

