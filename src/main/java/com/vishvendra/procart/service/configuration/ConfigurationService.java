package com.vishvendra.procart.service.configuration;

import com.vishvendra.procart.entities.Configuration;
import com.vishvendra.procart.model.ConfigurationDTO;
import com.vishvendra.procart.model.PageResultResponse;
import org.springframework.data.domain.PageRequest;

public interface ConfigurationService {

  PageResultResponse<ConfigurationDTO> getConfiguration(String key, PageRequest pageRequest);

  Configuration create(ConfigurationDTO configurationDTO);

  Configuration update(ConfigurationDTO configurationDTO);

  ConfigurationDTO getConfiguration(String key);

}
