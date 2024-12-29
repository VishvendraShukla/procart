package com.vishvendra.procart.mapper;

import com.vishvendra.procart.entities.Inventory;
import com.vishvendra.procart.model.InventoryResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface InventoryMapper {

  InventoryResponseDTO toDTO(Inventory inventory);

}
