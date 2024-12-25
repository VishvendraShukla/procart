package com.vishvendra.procart.service;

import com.vishvendra.procart.entities.Admin;
import com.vishvendra.procart.exception.ResourceNotFoundException;
import com.vishvendra.procart.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final AdminRepository adminRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Admin admin = this.adminRepository.findByUsername(username)
        .orElseThrow(
            () -> ResourceNotFoundException
                .create("Unauthorized.",
                    String.format("User not found with email: %s", username), 404));

    return User.withUsername(admin.getUsername())
        .accountExpired(admin.isDeleted())
        .accountLocked(admin.isDeleted())
        .password(admin.getPassword())
        .roles("ADMIN")
        .disabled(admin.isDeleted())
        .build();
  }
}