package com.vishvendra.procart.service;

import com.vishvendra.procart.entities.Admin;
import com.vishvendra.procart.entities.ProfileDetails;
import com.vishvendra.procart.exception.ResourceNotFoundException;
import com.vishvendra.procart.mapper.AdminMapper;
import com.vishvendra.procart.model.AdminDTO;
import com.vishvendra.procart.repository.AdminRepository;
import com.vishvendra.procart.repository.ProfileDetailsRepository;
import com.vishvendra.procart.utils.hash.HashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("adminService")
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final AdminMapper adminMapper;
  private final AdminRepository adminRepository;
  private final ProfileDetailsRepository profileDetailsRepository;
  private final HashService hashService;

  @Override
  @Transactional
  public AdminDTO createAdmin(AdminDTO adminDTO) {
    Admin admin = this.adminMapper.toEntity(adminDTO);
    admin.setPassword(this.hashService.encodePassword(adminDTO.getPassword()));
    if (adminDTO.getProfileDetailsDTO() != null) {
      ProfileDetails profileDetails = this.profileDetailsRepository.save(
          this.adminMapper.toEntity(adminDTO.getProfileDetailsDTO()));
      admin.setProfileDetails(profileDetails);
    }
    this.adminRepository.save(admin);
    AdminDTO response = this.adminMapper.toDTO(admin);
    response.setPassword(null);
    return response;
  }

  @Override
  public AdminDTO get(Long id) throws ResourceNotFoundException {
    Admin adminToReturn = this.adminRepository.findById(id)
        .orElseThrow(() -> ResourceNotFoundException
            .create("Please check inputted data and try again",
                String.format("Admin not found by id %s", id)));
    AdminDTO response = this.adminMapper.toDTO(adminToReturn);
    response.setPassword(null);
    return response;
  }

  @Override
  public AdminDTO retrieveByUsername(String username) throws ResourceNotFoundException {
    Admin adminToReturn = this.adminRepository.findByUsername(username)
        .orElseThrow(() -> ResourceNotFoundException
            .create("Please check inputted data and try again",
                String.format("Admin not found by username %s", username)));
    AdminDTO response = this.adminMapper.toDTO(adminToReturn);
    response.setPassword(null);
    return response;
  }
}
