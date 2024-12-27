package com.vishvendra.procart.repository;

import com.vishvendra.procart.entities.User;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User> {

  Optional<User> findByUsername(final String username);
}
