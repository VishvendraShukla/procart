package com.vishvendra.procart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ProductDTO extends AbstractDTO {

  private String name;
  private String description;
  private String sku;
  private BigDecimal amount;
  private String imageUrl;
  private Long quantity;
  private ProductCurrencyDTO currency;

}
