package com.github.wzclouds.online.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharpRemoveDTO {
  private List<String> ids;
}
