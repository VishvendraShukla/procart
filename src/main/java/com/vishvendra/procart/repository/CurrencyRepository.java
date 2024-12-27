package com.vishvendra.procart.repository;

import com.vishvendra.procart.entities.ProductCurrency;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends BaseRepository<ProductCurrency>,
    JpaSpecificationExecutor<ProductCurrency> {

}
