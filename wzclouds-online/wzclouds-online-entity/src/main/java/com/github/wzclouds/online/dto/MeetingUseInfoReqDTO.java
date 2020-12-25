package com.github.wzclouds.online.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会议室返回实体
 */
@Data
@NoArgsConstructor
@ApiModel(value = "MeetingUseInfoReqDTO", description = "使用中会议室列表入参实体")
public class MeetingUseInfoReqDTO {
    private String meetingName;

    @Builder
    public MeetingUseInfoReqDTO(String meetingName) {
        this.meetingName = meetingName;
    }
}
