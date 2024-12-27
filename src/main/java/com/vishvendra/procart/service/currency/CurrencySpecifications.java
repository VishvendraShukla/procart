package com.vishvendra.procart.service.currency;

import com.vishvendra.procart.entities.ProductCurrency;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class CurrencySpecifications {

  public static Specification<ProductCurrency> withFilters(String currencyCode) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (currencyCode != null) {
        predicates.add(criteriaBuilder.equal(root.get("code"), currencyCode));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
