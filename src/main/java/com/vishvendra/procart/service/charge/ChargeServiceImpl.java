package com.vishvendra.procart.service.charge;

import com.vishvendra.procart.entities.AuditAction;
import com.vishvendra.procart.entities.Charge;
import com.vishvendra.procart.entities.ChargeAmountType;
import com.vishvendra.procart.entities.ChargeAppliesOn;
import com.vishvendra.procart.entities.ChargeType;
import com.vishvendra.procart.event.AuditEvent;
import com.vishvendra.procart.event.EventDispatcher;
import com.vishvendra.procart.exception.IllegalInputException;
import com.vishvendra.procart.mapper.ChargeMapper;
import com.vishvendra.procart.model.ChargeDTO;
import com.vishvendra.procart.model.PageResultResponse;
import com.vishvendra.procart.repository.ChargeRepository;
import com.vishvendra.procart.utils.PlatformSecurityContext;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service("chargeService")
@RequiredArgsConstructor
public class ChargeServiceImpl implements ChargeService {

  private final ChargeMapper chargeMapper;
  private final ChargeRepository chargeRepository;
  private final EventDispatcher eventDispatcher;

  @Override
  public ChargeDTO create(ChargeDTO chargeDTO) {
    validateRequestData(chargeDTO);
    Charge charge = chargeRepository.save(chargeMapper.toEntity(chargeDTO));
    handleEvent("Charge " + charge.getDisplayName() + " created.", AuditAction.CREATE_CHARGE);
    return chargeMapper.toDTO(charge);
  }

  @Override
  public PageResultResponse<ChargeDTO> getWithFilters(Long id, String description,
      String displayName,
      PageRequest pageRequest) {
    Specification<Charge> chargeSpecification = ChargeSpecifications.withFilters(id, description,
        displayName);
    Page<Charge> charges = chargeRepository.findAll(chargeSpecification, pageRequest);
    return mapToDTOList(charges);
  }

  @Override
  public List<ChargeDTO> findChargesByAppliesOn(ChargeAppliesOn chargeAppliesOn) {
    List<Charge> charges = chargeRepository.findByChargeAppliesOn(chargeAppliesOn);
    return charges.stream().map(chargeMapper::toDTO).toList();
  }

  private PageResultResponse<ChargeDTO> mapToDTOList(Page<Charge> charges) {
    return PageResultResponse.of(
        charges.map(chargeMapper::toDTO).toList(),
        charges.getSize(),
        charges.getTotalElements(),
        charges.getNumber(),
        charges.isLast());
  }

  private void validateRequestData(ChargeDTO chargeDTO) {
    boolean isChargeAppliesOnValid = Arrays.stream(ChargeAppliesOn.values())
        .anyMatch(
            appliesOn -> appliesOn.name().equals(chargeDTO.getChargeAppliesOn().toUpperCase()));
    if (!isChargeAppliesOnValid) {
      throw IllegalInputException.create("Invalid charge applies on",
          "Invalid charge applies on" + chargeDTO.getChargeAppliesOn());
    }
    boolean isChargeTypeValid = Arrays.stream(ChargeType.values())
        .anyMatch(type -> type.name().equals(chargeDTO.getChargeType().toUpperCase()));
    if (!isChargeTypeValid) {
      throw IllegalInputException.create("Invalid charge type",
          "Invalid charge type" + chargeDTO.getChargeType());
    }
    boolean isChargeAmountTypeValid = Arrays.stream(ChargeAmountType.values())
        .anyMatch(type -> type.name().equals(chargeDTO.getChargeAmountType().toUpperCase()));
    if (!isChargeAmountTypeValid) {
      throw IllegalInputException.create("Invalid charge amount type",
          "Invalid charge amount type" + chargeDTO.getChargeAmountType());
    }
  }

  private void handleEvent(String message, AuditAction auditAction) {
    eventDispatcher.dispatchEvent(
        new AuditEvent(message,
            PlatformSecurityContext.getLoggedUser().getUsername(),
            auditAction));
  }
}
