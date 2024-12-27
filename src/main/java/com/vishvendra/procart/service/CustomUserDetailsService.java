package com.vishvendra.procart.service;

import com.vishvendra.procart.entities.Admin;
import com.vishvendra.procart.entities.User;
import com.vishvendra.procart.exception.ResourceNotFoundException;
import com.vishvendra.procart.repository.AdminRepository;
import com.vishvendra.procart.repository.UserRepository;
import com.vishvendra.procart.utils.securitymodel.CustomUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final AdminRepository adminRepository;
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    List<GrantedAuthority> authorities = new ArrayList<>();
    Optional<Admin> admin = this.adminRepository.findByUsername(username);
    if (admin.isEmpty()) {
      Optional<User> user = this.userRepository.findByUsername(username);
      if (user.isEmpty()) {
        throw ResourceNotFoundException.create("Unauthorized.",
            String.format("User/Admin not found by username %s", username), 404);
      }
      authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
      return new CustomUser(user.get(), authorities);
    }
    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    return new CustomUser(admin.get(), authorities);
  }
}