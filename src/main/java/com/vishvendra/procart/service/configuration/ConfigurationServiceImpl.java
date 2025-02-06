package com.vishvendra.procart.service.configuration;

import com.vishvendra.procart.entities.Configuration;
import com.vishvendra.procart.exception.ResourceNotFoundException;
import com.vishvendra.procart.model.ConfigurationDTO;
import com.vishvendra.procart.model.PageResultResponse;
import com.vishvendra.procart.repository.ConfigurationRepository;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service("configurationService")
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {

  private final ConfigurationRepository configurationRepository;

  @Override
  @Cacheable(value = "configurations", key = "#key", unless = "#result == null")
  public PageResultResponse<ConfigurationDTO> getConfiguration(String key,
      PageRequest pageRequest) {
    if (Objects.isNull(key)) {
      return map(this.configurationRepository.findAll(pageRequest));
    }

    Configuration configuration = this.configurationRepository.findByKey(key)
        .orElseThrow(() -> ResourceNotFoundException.create("Invalid Request",
            "Configuration not found with key: " + key));
    return PageResultResponse.of(
        Collections.singletonList(map(configuration)),
        10,
        1,
        0, true);
  }

  @Override
  @CachePut(value = "configurations", key = "#configurationDTO.key")
  public Configuration create(ConfigurationDTO configurationDTO) {
    clearCache();
    return this.configurationRepository.save(
        new Configuration(configurationDTO.getKey(), configurationDTO.getValue()));
  }

  @Override
  @CachePut(value = "configurations", key = "#configurationDTO.key")
  public Configuration update(ConfigurationDTO configurationDTO) {
    Configuration existingConfiguration = this.configurationRepository.findById(
            configurationDTO.getId())
        .orElseThrow(() -> ResourceNotFoundException.create("Invalid Request",
            "Configuration not found with ID: " + configurationDTO.getId()));

    existingConfiguration.setKey(configurationDTO.getKey());
    existingConfiguration.setValue(configurationDTO.getValue());
    existingConfiguration.setDeleted(configurationDTO.isDeleted());
    existingConfiguration.setUpdatedAt(configurationDTO.getUpdatedAt());
    clearCache();
    return this.configurationRepository.save(existingConfiguration);
  }

  @Override
  @Cacheable(value = "configurations", key = "#key", unless = "#result == null")
  public ConfigurationDTO getConfiguration(String key) {
    Configuration configuration = this.configurationRepository.findByKey(key)
        .orElseThrow(() -> ResourceNotFoundException.create("Invalid Request",
            "Configuration not found with key: " + key));
    return map(configuration);
  }

  @CacheEvict(value = "configurations", allEntries = true)
  public void clearCache() {
  }

  private PageResultResponse<ConfigurationDTO> map(Page<Configuration> configurations) {

    List<ConfigurationDTO> configurationDTOList = configurations.map(this::map).toList();
    return PageResultResponse.of(
        configurationDTOList,
        configurations.getSize(),
        configurations.getTotalElements(),
        configurations.getNumber(), configurations.isLast());
  }

  private ConfigurationDTO map(Configuration configuration) {
    ConfigurationDTO configurationDTO = new ConfigurationDTO();
    configurationDTO.setKey(configuration.getKey());
    configurationDTO.setValue(configuration.getValue());
    configurationDTO.setId(configuration.getId());
    configurationDTO.setDeleted(configuration.isDeleted());
    configurationDTO.setUpdatedAt(configuration.getUpdatedAt());
    configurationDTO.setCreatedAt(configuration.getCreatedAt());
    return configurationDTO;
  }

}
