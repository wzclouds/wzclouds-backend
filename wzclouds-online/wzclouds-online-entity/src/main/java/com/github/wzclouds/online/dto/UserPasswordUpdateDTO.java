package com.github.wzclouds.online.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserPasswordUpdateDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    @Builder
    public UserPasswordUpdateDTO(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
}
