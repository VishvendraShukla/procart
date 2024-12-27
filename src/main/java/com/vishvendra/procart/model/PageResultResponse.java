package com.vishvendra.procart.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageResultResponse<T> {

  private List<T> elements;
  private int pageSize;
  private long total;
  private int currentPage;
  private boolean last;

  public static <T> PageResultResponse<T> of(List<T> elements, int pageSize, long total, int currentPage, boolean last) {
    return new PageResultResponse<>(elements, pageSize, total, currentPage, last);
  }
}