package com.github.wzclouds.online.enumeration;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 实体注释中生成的类型枚举
 * 用户表
 * </p>
 *
 * @author wz
 * @date 2020-11-03
 */
@Getter
@AllArgsConstructor
@ApiModel(value = "UserType", description = "用户类型 #UserType{TEMP:临时用户;OUTTER:外部用户-枚举")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserType {

    /**
     * TEMP="临时用户"
     */
    TEMP("临时用户"),
    /**
     * OUTTER="外部用户"
     */
    OUTTER("外部用户"),
    ;

    @ApiModelProperty(value = "描述")
    private String desc;


    public static UserType match(String val, UserType def) {
        for (UserType enm : UserType.values()) {
            if (enm.name().equalsIgnoreCase(val)) {
                return enm;
            }
        }
        return def;
    }

    public static UserType get(String val) {
        return match(val, null);
    }

    public boolean eq(String val) {
        return this.name().equalsIgnoreCase(val);
    }

    public boolean eq(UserType val) {
        if (val == null) {
            return false;
        }
        return eq(val.name());
    }

    @ApiModelProperty(value = "编码", allowableValues = "TEMP,OUTTER", example = "TEMP")
    public String getCode() {
        return this.name();
    }

}
