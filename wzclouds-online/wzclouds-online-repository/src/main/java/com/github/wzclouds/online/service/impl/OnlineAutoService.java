package com.github.wzclouds.online.service.impl;

import com.github.wzclouds.common.UserSession;
import com.github.wzclouds.utils.ClientType;
import com.github.wzclouds.utils.JsonEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnlineAutoService {
    @Autowired
    private ConcurrentHashMap<String, UserSession> userSessions;

    /**
     * 心跳检测
     *
     * @param jsonEntity
     */
    public void ping(UserSession userSession, JsonEntity jsonEntity) {
        return;
    }

    public void sayHello(UserSession userSession, JsonEntity jsonEntity) {
        String targetIds = jsonEntity.getTargetIds();
        if (StringUtils.isNotEmpty(targetIds)) {
            List<String> targetArr = Arrays.asList(targetIds.split(","));
            for (UserSession u : this.userSessions.values()) {
                if (targetArr.contains(u.getUserId())) {
                    JsonEntity entity = JsonEntity.builder()
                            .eventName(jsonEntity.getEventName())
                            .clientFunc(jsonEntity.getClientFunc())
                            .data(jsonEntity.getData())
                            .build();
                    entity.sendResMsg(u);
                }
            }
        }
    }

    public void onTalk(UserSession userSession, JsonEntity jsonEntity) {
        if (StringUtils.isEmpty(jsonEntity.getUserSession().getMeetingId())) {
            Map<String, Object> data = jsonEntity.getData();
            data.put("msg", "不可在大厅发言");
            JsonEntity entity = JsonEntity.builder()
                    .eventName(jsonEntity.getEventName())
                    .clientFunc(ClientType.on_talk_system)
                    .data(data)
                    .build();
            entity.sendResMsg(jsonEntity.getUserSession());
            return;
        }
        String targetIds = jsonEntity.getTargetIds();
        List<String> targetArr = Arrays.asList(targetIds.split(","));
        for (UserSession u : this.userSessions.values()) {
            if (StringUtils.isNotEmpty(targetIds) && !targetArr.contains(u.getUserId())) {
                continue;
            }
            if (jsonEntity.getUserSession().getMeetingId().equals(u.getMeetingId())) {
                JsonEntity entity = JsonEntity.builder()
                        .eventName(jsonEntity.getEventName())
                        .clientFunc(jsonEntity.getClientFunc())
                        .data(jsonEntity.getData())
                        .clientFunc(ClientType.on_talk)
                        .build();
                entity.sendResMsg(u);
            }
        }
    }

    /**
     * 同步路径
     *
     * @param jsonEntity
     */
    public void sharpPathDraw(UserSession userSession, JsonEntity jsonEntity) {
        jsonEntity.setClientFunc(ClientType.retSharpPathDraw);
        for (UserSession u : this.userSessions.values()) {
            if (userSession.getMeetingId().equals(u.getMeetingId())) {
                jsonEntity.sendResMsg(u);
            }
        }
    }

    /**
     * 删除sharp
     *
     * @param jsonEntity
     */
    public void sharpRemove(UserSession userSession, JsonEntity jsonEntity) {
        jsonEntity.setClientFunc(ClientType.retSharpRemove);
        for (UserSession u : this.userSessions.values()) {
            if (userSession.getMeetingId().equals(u.getMeetingId())) {
                jsonEntity.sendResMsg(u);
            }
        }
    }

    /**
     * sharp变换
     *
     * @param jsonEntity
     */
    public synchronized void sharpTranslete(UserSession userSession, JsonEntity jsonEntity) {
        jsonEntity.setClientFunc(ClientType.retSharpTranslete);
        for (UserSession u : this.userSessions.values()) {
            if (userSession.getMeetingId().equals(u.getMeetingId())) {
                jsonEntity.sendResMsg(u);
            }
        }
    }
}
