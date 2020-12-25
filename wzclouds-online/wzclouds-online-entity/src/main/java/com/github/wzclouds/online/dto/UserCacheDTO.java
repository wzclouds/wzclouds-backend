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
@ApiModel(value = "UserCacheDTO", description = "用户缓存实体")
public class UserCacheDTO {
    private Long id;
    private String account;
    private String name;
    private Long outUserId;

    @Builder
    public UserCacheDTO(Long id, String account, String name, Long outUserId) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.outUserId = outUserId;
    }
}
