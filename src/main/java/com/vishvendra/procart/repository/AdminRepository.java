package com.vishvendra.procart.repository;

import com.vishvendra.procart.entities.Admin;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends BaseRepository<Admin> {
  Optional<Admin> findByUsername(final String username);
}
