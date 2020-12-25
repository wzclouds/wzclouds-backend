package com.github.wzclouds.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.wzclouds.utils.ServerType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FileUploadType {
    SHARP_IMG("SHARP_IMG", "画板上传图片"),
    BIZ_FILE("BIZ_FILE", "业务文件");

    private String desc;

    private String code;

    public static ServerType match(String val, ServerType def) {
        for (ServerType enm : ServerType.values()) {
            if (enm.name().equalsIgnoreCase(val)) {
                return enm;
            }
        }
        return def;
    }

    public static ServerType get(String val) {
        return match(val, null);
    }

    public boolean eq(String val) {
        return this.name().equalsIgnoreCase(val);
    }

    public boolean eq(ServerType val) {
        if (val == null) {
            return false;
        }
        return eq(val.name());
    }

    public String getCode() {
        return this.name();
    }
}
