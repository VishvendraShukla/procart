package com.vishvendra.procart.service;

import com.vishvendra.procart.exception.ResourceNotFoundException;
import com.vishvendra.procart.model.AdminDTO;

public interface AdminService {

  AdminDTO createAdmin(AdminDTO admin);

  AdminDTO get(Long id) throws ResourceNotFoundException;

  AdminDTO retrieveByUsername(String username) throws ResourceNotFoundException;

}
