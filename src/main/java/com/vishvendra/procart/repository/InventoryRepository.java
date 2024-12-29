package com.vishvendra.procart.repository;

import com.vishvendra.procart.entities.Inventory;
import com.vishvendra.procart.entities.Product;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends BaseRepository<Inventory>,
    JpaSpecificationExecutor<Inventory> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT i FROM Inventory i WHERE i.product = :product")
  Inventory findByProductWithLock(@Param("product") Product product);

  Optional<Inventory> findByProduct(Product product);
}

