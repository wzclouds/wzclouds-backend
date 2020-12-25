package com.github.wzclouds.online.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.wzclouds.online.entity.Sharp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
@Data
@TableName("b_sharp")
@NoArgsConstructor
@AllArgsConstructor
public class SharpTransDTO extends Sharp {
  private static final long serialVersionUID = 1L;
  private List<List<Integer>> path;
  private List<Double> matrix;
  private List<Double> centPoint;
  private String createId;
}
