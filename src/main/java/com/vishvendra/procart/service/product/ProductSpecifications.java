package com.vishvendra.procart.service.product;


import com.vishvendra.procart.entities.Product;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

  public static Specification<Product> withFilters(Long id, String name, String sku) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (id != null) {
        predicates.add(criteriaBuilder.equal(root.get("id"), id));
      }
      if (name != null) {
        predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
      }
      if (sku != null) {
        predicates.add(criteriaBuilder.equal(root.get("sku"), sku));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
