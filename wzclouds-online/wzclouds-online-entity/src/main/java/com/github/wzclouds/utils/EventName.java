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
public enum EventName implements EnumValue {
    //用户加入
    __join("__join", "用户加入") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //用户加入
    __joinUser("__joinUser", "用户加入") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //用户加入会议
    __joinVideo("__joinVideo", "用户加入会议") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //offer提供
    __offer("__offer", "offer提供") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //answer提供
    __answer("__answer", "answer提供") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //ice提供
    __ice_candidate("__ice_candidate", "ice认证") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //获取在线列表
    __getOnlineList("__getOnlineList", "获取在线列表") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //通知相关
    NOTICE("NOTICE", "通知相关") {
        @Override
        public String code() {
            return getCode();
        }
    },

    MEET_REQUEST("MEET_REQUEST", "通知相关") {
        @Override
        public String code() {
            return getCode();
        }
    },

//    onTalk("onTalk", "即时通讯") {
//        @Override
//        public String code() {
//            return getCode();
//        }
//    },


    //-----------------------------------------------------------------以下为返回type
    // 用户离开客户端回调
    _remove_peer("_remove_peer", "用户离开客户端回调") {
        @Override
        public String code() {
            return getCode();
        }
    },

    // 新用户加入
    _new_peer("_new_peer", "新用户加入") {
        @Override
        public String code() {
            return getCode();
        }
    },

    // 新用户加入_源响应
    _peers("_peers", "新用户加入_源响应") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //offer提供-反应
    _offer("_offer", "offer提供") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //answer提供-反应
    _answer("_answer", "answer提供") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //ice提供-反应
    _ice_candidate("_ice_candidate", "ice认证") {
        @Override
        public String code() {
            return getCode();
        }
    },

    //获取在线列表-反应
    _getOnlineList("_getOnlineList", "answer提供") {
        @Override
        public String code() {
            return getCode();
        }
    },
    ;




























    private String desc;

    private String code;

    public static EventName match(String val, EventName def) {
        for (EventName enm : EventName.values()) {
            if (enm.name().equalsIgnoreCase(val)) {
                return enm;
            }
        }
        return def;
    }

    public static EventName get(String val) {
        return match(val, null);
    }

    public boolean eq(String val) {
        return this.name().equalsIgnoreCase(val);
    }

    public boolean eq(EventName val) {
        if (val == null) {
            return false;
        }
        return eq(val.name());
    }

    public String getCode() {
        return this.name();
    }
}
