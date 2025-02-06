package com.vishvendra.procart.repository;

import com.vishvendra.procart.entities.Configuration;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends BaseRepository<Configuration> {

  Optional<Configuration> findByKey(String key);
}
