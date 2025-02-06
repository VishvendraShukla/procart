package com.vishvendra.procart.service.transaction;

import com.vishvendra.procart.entities.Transaction;
import com.vishvendra.procart.entities.TransactionStatus;
import com.vishvendra.procart.repository.TransactionRepository;
import com.vishvendra.procart.utils.payment.TransactionDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("transactionService")
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  @Override
  @Transactional
  public Transaction createTransaction(TransactionDTO transactionDTO) {
    Transaction transaction = Transaction
        .builder()
        .cart(transactionDTO.getCart())
        .charges(transactionDTO.getCharges())
        .user(transactionDTO.getUser())
        .status(transactionDTO.getStatus() == null ? TransactionStatus.PENDING
            : transactionDTO.getStatus())
        .description(transactionDTO.getDescription())
        .referenceId(transactionDTO.getReferenceId())
        .traceId(UUID.randomUUID().toString())
        .totalAmount(transactionDTO.getTotalAmount())
        .currency(transactionDTO.getCurrency())
        .referenceName(transactionDTO.getReferenceName())
        .build();
    transactionRepository.save(transaction);
    return transaction;
  }

  @Override
  public Transaction updateTransaction(Transaction transaction) {
    return transactionRepository.save(transaction);
  }

  @Override
  public Transaction getTransactionByReferenceId(String referenceId) {
    return this.transactionRepository.findByReferenceId(referenceId);
  }
}
