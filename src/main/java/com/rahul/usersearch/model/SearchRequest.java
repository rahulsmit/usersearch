package com.rahul.usersearch.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Getter
@Setter
public class SearchRequest {

  String search;
  @Min(value = 1)
  @Max(value = 100)
  Integer minAge;

  @Min(value = 1)
  @Max(value = 100)
  Integer maxAge;

  @Min(value = 1)
  int pageNumber = 1;

  @Override
  public String toString() {
    return "SearchRequest{" +
        "search='" + search + '\'' +
        ", minAge=" + minAge +
        ", pageNumber=" + pageNumber +
        '}';
  }
}
