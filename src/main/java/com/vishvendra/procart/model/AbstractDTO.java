package com.vishvendra.procart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public abstract class AbstractDTO {

  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private boolean deleted;

}
