package com.vishvendra.procart.repository;

import com.vishvendra.procart.entities.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends BaseRepository<Transaction> {

  Transaction findByReferenceId(String referenceId);
}
