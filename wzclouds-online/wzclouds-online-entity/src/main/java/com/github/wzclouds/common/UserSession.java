package com.github.wzclouds.common;

import com.alibaba.fastjson.JSON;
import com.github.wzclouds.utils.JsonEntity;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;

/**
 * User session.
 *
 * @author Boni Garcia (bgarcia@gsyc.es)
 * @since 5.0.0
 */
@Data
public class UserSession {

    private static final Logger log = LoggerFactory.getLogger(UserSession.class);

    private String userId;
    private String token;
    private String name;
    private String photo;

    private final WebSocketSession session;
    private String meetingPassword;
    private String meetingId;

    public UserSession(WebSocketSession session, JsonEntity je) {
        this.session = session;
        Map<String, Object> data = je.getData();

        if (data.get("meetingId") != null && StringUtils.isNotEmpty(String.valueOf(data.get("meetingId")))) {
            this.setMeetingId(String.valueOf(data.get("meetingId")));
        }
        if (data.get("meetingPassword") != null && StringUtils.isNotEmpty(String.valueOf(data.get("meetingPassword")))) {
            this.setMeetingPassword(String.valueOf(data.get("meetingPassword")));
        }
    }

    public void update(JsonEntity je) {
        Map<String, Object> data = je.getData();
        if (data.get("meetingId") != null && StringUtils.isNotEmpty(String.valueOf(data.get("meetingId")))) {
            this.setMeetingId(String.valueOf(data.get("meetingId")));
        }
        if (data.get("meetingPassword") != null && StringUtils.isNotEmpty(String.valueOf(data.get("meetingPassword")))) {
            this.setMeetingPassword(String.valueOf(data.get("meetingPassword")));
        }
    }

    public UserSession(WebSocketSession session) {
        this.session = session;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void sendMessage(JSON message) throws IOException, EncodeException {
        log.info("Sending message from user with session Id '{}': {}", session.getId(), message);
        session.sendMessage(new TextMessage(message.toJSONString()));
    }
}
