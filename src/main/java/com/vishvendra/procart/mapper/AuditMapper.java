package com.vishvendra.procart.mapper;

import com.vishvendra.procart.entities.Audit;
import com.vishvendra.procart.model.AuditLogsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuditMapper {
  AuditLogsDTO toDTO(Audit audit);
}
