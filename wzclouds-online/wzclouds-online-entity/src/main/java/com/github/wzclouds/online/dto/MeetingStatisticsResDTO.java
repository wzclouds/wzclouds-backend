package com.github.wzclouds.online.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会议室返回实体
 */
@Data
@NoArgsConstructor
@ApiModel(value = "MeetingStatisticsResDTO", description = "会议室返回实体")
public class MeetingStatisticsResDTO {
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "是否加密")
    private Boolean isPassword;

    @ApiModelProperty(value = "会议id")
    private String meetingId;

    @ApiModelProperty(value = "会议名称")
    private String meetingName;

    @ApiModelProperty(value = "描述")
    private String descs;

    @ApiModelProperty(value = "拥有者系统id")
    private Long owner;

    @ApiModelProperty(value = "当前人数")
    private Integer num;

    @ApiModelProperty(value = "最大客户端人数")
    private Integer maxClient = 8;

    @ApiModelProperty(value = "是否已满")
    private Boolean isFull;

    @Builder
    public MeetingStatisticsResDTO(Integer num, String password, Boolean isPassword, String meetingId, String meetingName, String descs, Long owner, Integer maxClient, Boolean isFull) {
        this.num = num;
        this.password = password;
        this.isPassword = isPassword;
        this.meetingId = meetingId;
        this.meetingName = meetingName;
        this.descs = descs;
        this.owner = owner;
        this.maxClient = maxClient;
        this.isFull = isFull;
    }

    public Integer getMaxClient() {
        if (this.maxClient == null) {
            return 8;
        }
        return this.maxClient;
    }

    public Boolean getIsFull() {
        if (getMaxClient() == null || getNum() == null) {
            return false;
        }
        return (getMaxClient() <= getNum());
    }
}
