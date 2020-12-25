package com.github.wzclouds.online.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResDTO {
    private String photo;
    private String type;
    private String userId;
    private String userName;
    private String token;
    private String refreshToken;
    private String videolist;

    @Builder
    public UserResDTO(String photo, String type, String userId, String userName, String token, String refreshToken, String videolist) {
        this.photo = photo;
        this.type = type;
        this.userId = userId;
        this.userName = userName;
        this.token = token;
        this.refreshToken = refreshToken;
        this.videolist = videolist;
    }
}
