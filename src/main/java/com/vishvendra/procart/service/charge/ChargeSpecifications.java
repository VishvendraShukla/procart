package com.vishvendra.procart.service.charge;

import com.vishvendra.procart.entities.Charge;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class ChargeSpecifications {

  public static Specification<Charge> withFilters(Long id, String chargeDescription,
      String chargeDisplayName) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (id != null) {
        predicates.add(criteriaBuilder.equal(root.get("id"), id));
      }
      if (chargeDescription != null) {
        predicates.add(
            criteriaBuilder.like(root.get("description"), "%" + chargeDescription + "%"));
      }
      if (chargeDisplayName != null) {
        predicates.add(criteriaBuilder.equal(root.get("displayName"), chargeDisplayName));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  public static Specification<Charge> isDeletedFalse() {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), false);
  }

}
