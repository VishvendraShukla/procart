package com.vishvendra.procart.service.transaction;

import com.vishvendra.procart.entities.Transaction;
import com.vishvendra.procart.utils.payment.TransactionDTO;

public interface TransactionService {

  Transaction createTransaction(TransactionDTO transactionDTO);

  Transaction updateTransaction(Transaction transaction);

  Transaction getTransactionByReferenceId(String referenceId);

}
