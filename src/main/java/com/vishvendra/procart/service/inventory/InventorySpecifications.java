package com.vishvendra.procart.service.inventory;

import com.vishvendra.procart.entities.Inventory;
import com.vishvendra.procart.entities.Product;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class InventorySpecifications {

  public static Specification<Inventory> withFilters(Long inventoryId, Product product) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (inventoryId != null) {
        predicates.add(criteriaBuilder.equal(root.get("id"), inventoryId));
      }
      if (product != null) {
        predicates.add(criteriaBuilder.equal(root.get("product"), product));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}


