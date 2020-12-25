package com.github.wzclouds.constant;

import com.github.wzclouds.exception.code.BaseExceptionCode;

public enum  OnlineException implements BaseExceptionCode {
    //权限相关 start
    LOGIN_HAS(10000, "用户重复登录!"),

    MEETING_HAS_PASSWORD(10001, "房间需要输入密码!"),
    MEETING_PASSWORD_ERR(10002, "房间密码错误!"),
    MEETING_CLIENT_MAX_OUT(10003, "房间人数超过上限!")
    ;

    private int code;
    private String msg;

    OnlineException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }


    public OnlineException build(String msg, Object... param) {
        this.msg = String.format(msg, param);
        return this;
    }

    public OnlineException param(Object... param) {
        this.msg = String.format(msg, param);
        return this;
    }
}
