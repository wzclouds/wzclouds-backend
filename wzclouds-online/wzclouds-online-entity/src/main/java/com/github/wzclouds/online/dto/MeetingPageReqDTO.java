package com.github.wzclouds.online.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 会议室分页查询入参
 */
@Data
@ApiModel(value = "MeetingPageReqDTO", description = "会议室分页查询入参")
public class MeetingPageReqDTO {
    @ApiModelProperty(value = "会议室名称")
    private String name;
}
