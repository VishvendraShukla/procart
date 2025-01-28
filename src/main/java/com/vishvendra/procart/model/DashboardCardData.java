package com.vishvendra.procart.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DashboardCardData {

  private String title;
  private String value;
  private String icon;
  private String color;

}
