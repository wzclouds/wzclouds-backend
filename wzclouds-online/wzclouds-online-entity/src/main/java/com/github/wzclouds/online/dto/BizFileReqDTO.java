package com.github.wzclouds.online.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@ToString(callSuper = true)
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BizFileReqDTO", description = "文件查询入参")
public class BizFileReqDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件名")
    private String name;

    @ApiModelProperty(value = "业务id")
    private String bizId;

    @ApiModelProperty(value = "业务id")
    private String meetingId;
}
