package com.rahul.usersearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReindexResult {
  private int totalInIndex;
  private int totalAtSource;
}
