package com.vishvendra.procart.repository;

import com.vishvendra.procart.entities.Charge;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeRepository extends BaseRepository<Charge>,
    JpaSpecificationExecutor<Charge> {

}
