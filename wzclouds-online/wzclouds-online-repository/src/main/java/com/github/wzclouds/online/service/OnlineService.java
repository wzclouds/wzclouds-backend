package com.github.wzclouds.online.service;

import com.github.wzclouds.common.UserSession;
import com.github.wzclouds.online.dto.MeetingStatisticsResDTO;
import com.github.wzclouds.utils.JsonEntity;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;

public interface OnlineService {
    void isLogin(UserSession userSession);

    void joinMeeting(UserSession userSession);

    void meetingValidate(UserSession userSession);

    void joinVideo(UserSession userSession);

    void joinHall();

    void joinSuccess(UserSession userSession);

    void joinError(UserSession userSession, String msg);

    void removeSocket(UserSession userSession);

    void doOffer(UserSession userSession, Map<String, Object> data);

    void doAnswer(UserSession userSession, Map<String, Object> data);

    void doIce(UserSession userSession, Map<String, Object> data);

    List<MeetingStatisticsResDTO> getAllMeeting();

    void invokeMethod(UserSession userSession, JsonEntity jsonEntity);

    void close(WebSocketSession session, String msg);
}
