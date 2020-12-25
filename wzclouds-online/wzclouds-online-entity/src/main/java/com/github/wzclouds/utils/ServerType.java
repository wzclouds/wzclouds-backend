package com.github.wzclouds.utils;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.wzclouds.utils.converter.EnumValue;
import com.github.wzclouds.utils.converter.EnumValueDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JSONType(deserializer = EnumValueDeserializer.class)
public enum ServerType implements EnumValue {

    sayHello("sayHello", "邀请用户加入会议") {
        @Override
        public String code() {
            return getCode();
        }
    },

    onTalk("onTalk", "文字聊天") {
        @Override
        public String code() {
            return getCode();
        }
    },

    ping("ping", "心跳检测ping") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //绘制绘线
    sharpPathDraw("sharpPathDraw","画板绘线") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //绘制清除
    sharpRemove("sharpRemove","画板清除") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //绘制转换
    sharpTranslete("sharpTranslete","画板清除") {
        @Override
        public String code() {
            return getCode();
        }
    };

    ;

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
