package com.vishvendra.procart.utils.payment;

import com.vishvendra.procart.entities.Cart;
import com.vishvendra.procart.entities.Charge;
import com.vishvendra.procart.entities.TransactionStatus;
import com.vishvendra.procart.entities.User;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

  private String currency;
  private BigDecimal totalAmount;
  private String referenceId;
  private String traceId;
  private String referenceName;
  private String description;
  private TransactionStatus status;
  private Cart cart;
  private User user;
  private List<Charge> charges;

}
