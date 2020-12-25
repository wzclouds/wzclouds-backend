package com.github.wzclouds.online.dto;

import com.github.wzclouds.online.entity.Meeting;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 会议室新增入参
 */
@Data
@ApiModel(value = "MeetingSaveReqDTO", description = "会议室新增入参")
public class MeetingSaveReqDTO extends Meeting {
//    @ApiModelProperty(value = "会议标签")
//    private List<String> tag;
}
