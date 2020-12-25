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
public enum ClientType implements EnumValue {

    retHello("retHello", "回调-邀请用户加入会议") {
        @Override
        public String code() {
            return getCode();
        }
    },

    notice_reject("notice_reject", "回调-用户移出会议") {
        @Override
        public String code() {
            return getCode();
        }
    },

    hallClients("hallClients", "回调-获取大厅用户方法") {
        @Override
        public String code() {
            return getCode();
        }
    },

    meetingClients("meetingClients", "回调-获取当前会议用户方法") {
        @Override
        public String code() {
            return getCode();
        }
    },

    serverMeetingStatistics("serverMeetingStatistics", "执行前端会议室列表方法") {
        @Override
        public String code() {
            return getCode();
        }
    },

    on_talk("on_talk", "接收到文字信息(用户)") {
        @Override
        public String code() {
            return getCode();
        }
    },

    on_talk_system("on_talk_system", "接收到文字信息(系统)") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //用户加入
    join_success("join_success", "用户加入成功回调") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //验证会议最大人数
    meeting_client_out("meeting_client_out", "当前会议超过设置最大人数,无法进入!") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //验证房间密码成功
    meeting_password_success("meeting_password_success", "验证成功!") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //验证房间密码失败
    meeting_password_error("meeting_password_error", "验证房间密码失败!") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //用户加入失败
    join_error("join_error", "用户加入失败回调") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //绘制绘线
    retSharpPathDraw("retSharpPathDraw","画板绘线") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //绘制清除
    retSharpRemove("retSharpRemove","画板清除") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //绘制转换
    retSharpTranslete("retSharpTranslete","画板清除") {
        @Override
        public String code() {
            return getCode();
        }
    };
    ;




























    private String desc;

    private String code;

    public static ClientType match(String val, ClientType def) {
        for (ClientType enm : ClientType.values()) {
            if (enm.name().equalsIgnoreCase(val)) {
                return enm;
            }
        }
        return def;
    }

    public static ClientType get(String val) {
        return match(val, null);
    }

    public boolean eq(String val) {
        return this.name().equalsIgnoreCase(val);
    }

    public boolean eq(ClientType val) {
        if (val == null) {
            return false;
        }
        return eq(val.name());
    }

    public String getCode() {
        return this.name();
    }
}
