package com.vishvendra.procart.repository;

import com.vishvendra.procart.entities.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseRepository<Product>,
    JpaSpecificationExecutor<Product> {

}
