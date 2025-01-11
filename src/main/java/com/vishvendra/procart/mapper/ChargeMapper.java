package com.vishvendra.procart.mapper;

import com.vishvendra.procart.entities.Charge;
import com.vishvendra.procart.model.ChargeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChargeMapper {

  Charge toEntity(ChargeDTO chargeDTO);

  ChargeDTO toDTO(Charge charge);
}
