package com.github.wzclouds.online.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.wzclouds.common.UserSession;
import com.github.wzclouds.exception.BizException;
import com.github.wzclouds.online.dto.UserResDTO;
import com.github.wzclouds.online.entity.User;
import com.github.wzclouds.online.service.OnlineService;
import com.github.wzclouds.online.service.UserService;
import com.github.wzclouds.utils.JsonEntity;
import com.github.wzclouds.utils.converter.MyParserConfig;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Slf4j
public class OnlineSocket extends TextWebSocketHandler {
    /**
     * 引入依赖包，创建线程池
     */
    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
    private ExecutorService fixedThreadPool = new ThreadPoolExecutor(30, 50, 200L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), namedThreadFactory);

    @Autowired
    private ConcurrentHashMap<String, UserSession> userSessions;

    @Autowired
    private UserService userService;

    @Autowired
    private OnlineService onlineService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        Runnable t = () -> {
            JsonEntity jsonEntity = JSONObject.parseObject(message.getPayload().trim(), JsonEntity.class, new MyParserConfig());
            UserSession userSession;
            Map<String, Object> data = jsonEntity.getData();
            switch (jsonEntity.getEventName()) {
                case __joinUser:
                    String token = String.valueOf(data.get("token"));
                    UserResDTO userDTO = new UserResDTO();
                    if (token.startsWith("tmp") && token.length() == 21) {
                        String name = String.valueOf(data.get("name"));
                        String photo = String.valueOf(data.get("photo"));
                        userDTO.setUserId(token);
                        userDTO.setUserName(name);
                        userDTO.setPhoto(photo);
                    }

                    if (userSessions.containsKey(session.getId())) {
                        userSession = userSessions.get(session.getId());
                        userSession.update(jsonEntity);
                    } else {
                        try {
                            if (StringUtils.isEmpty(userDTO.getUserId())) {
                                User user = userService.decode(token);
                                userDTO.setUserId(String.valueOf(user.getId()));
                                userDTO.setUserName(user.getName());
                                userDTO.setPhoto(user.getPhoto());
                            }
                        } catch (Exception e) {
                            userSession = new UserSession(session);
                            onlineService.joinError(userSession, e.getMessage());
                            return;
                        }

                        userSession = new UserSession(session, jsonEntity);
                        userSession.setUserId(userDTO.getUserId());
                        userSession.setName(userDTO.getUserName());
                        userSession.setPhoto(userDTO.getPhoto());

                        // 判断重复登录
                        onlineService.isLogin(userSession);
                        userSessions.put(session.getId(), userSession);
                        log.info(userSession.getName() + "加入系统");
                    }


                    // 做房间验证
                    onlineService.meetingValidate(userSession);
                    if (StringUtils.isNotEmpty(userSession.getMeetingId()) && !"-1".equals(userSession.getMeetingId())) {
                        log.info(userSession.getName() + "加入房间:" + userSession.getMeetingId());
                        onlineService.joinMeeting(userSession);
                    } else {
                        onlineService.joinHall();
                    }

                    // 加入socket成功回调
                    onlineService.joinSuccess(userSession);
                    break;
                case __joinVideo:
                    userSession = getUserSession(session);
                    onlineService.joinVideo(userSession);
                    break;
                case __answer:
                    userSession = getUserSession(session);
                    log.info(userSession.getName() + "发起answer请求");
                    onlineService.doAnswer(userSession, data);
                    break;
                case __offer:
                    userSession = getUserSession(session);
                    log.info(userSession.getName() + "发起offer请求");
                    onlineService.doOffer(userSession, data);
                    break;
                case __ice_candidate:
                    userSession = getUserSession(session);
                    log.info(userSession.getName() + "发起ice请求");
                    onlineService.doIce(userSession, data);
                    break;
                case NOTICE:
                    userSession = getUserSession(session);
                    log.info(userSession.getName() + "发起NOTICE请求");
                    onlineService.invokeMethod(userSession, jsonEntity);
                    break;
                default:
                    break;
            }
        };

        fixedThreadPool.submit(t);
    }

    private UserSession getUserSession(WebSocketSession session) {
        UserSession userSession;
        if (userSessions.containsKey(session.getId())) {
            userSession = userSessions.get(session.getId());
            return userSession;
        } else {
            throw BizException.wrap("不存在该socket");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        UserSession userSession = userSessions.get(session.getId());
        if (userSession != null) {
            log.info(userSession.getName() + "退出了");
            userService.removeOnlineCache(userSession);
        }
    }


}
