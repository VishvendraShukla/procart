package com.vishvendra.procart.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class InventoryResponseDTO extends AbstractDTO {

  private ProductDTO product;
  private Long totalStock;
  private Long lockedStock;
  private Long availableStock;

}
