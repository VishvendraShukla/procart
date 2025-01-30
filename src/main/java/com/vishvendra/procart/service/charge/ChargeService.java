package com.vishvendra.procart.service.charge;

import com.vishvendra.procart.entities.ChargeAppliesOn;
import com.vishvendra.procart.model.ChargeDTO;
import com.vishvendra.procart.model.PageResultResponse;
import java.util.List;
import org.springframework.data.domain.PageRequest;

public interface ChargeService {

  ChargeDTO create(ChargeDTO chargeDTO);

  PageResultResponse<ChargeDTO> getWithFilters(Long id, String description, String displayName,
      PageRequest pageRequest);

  List<ChargeDTO> findChargesByAppliesOn(ChargeAppliesOn chargeAppliesOn);

}
