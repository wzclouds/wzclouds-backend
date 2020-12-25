package com.github.wzclouds.utils;

import com.alibaba.fastjson.JSON;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class ServerEncoder implements Encoder.Text<JsonEntity> {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(EndpointConfig arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public String encode(JsonEntity socketMsg) {
        try {
            return JSON.toJSONString(socketMsg);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

}
