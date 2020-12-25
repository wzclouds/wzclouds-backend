package com.github.wzclouds.online.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.wzclouds.common.UserSession;
import com.github.wzclouds.constant.CacheKey;
import com.github.wzclouds.constant.OnlineException;
import com.github.wzclouds.exception.BizException;
import com.github.wzclouds.online.dto.MeetingStatisticsResDTO;
import com.github.wzclouds.online.service.MeetingService;
import com.github.wzclouds.online.service.OnlineService;
import com.github.wzclouds.utils.ClientType;
import com.github.wzclouds.utils.EventName;
import com.github.wzclouds.utils.JsonEntity;
import com.github.wzclouds.utils.SpringUtils;
import com.github.wzclouds.dozer.DozerUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OnlineServiceImpl implements OnlineService {
    @Autowired
    private ConcurrentHashMap<String, UserSession> userSessions;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DozerUtils dozerUtils;

    @Override
    public void isLogin(UserSession userSession) {
        userSessions.forEach((u, item) -> {
            if (item.getUserId().equals(userSession.getUserId())) {
                joinError(userSession, OnlineException.LOGIN_HAS.getMsg());
            }
        });
    }

    /**
     * 加入房间
     *
     * @param userSession
     */
    @Override
    public void joinMeeting(UserSession userSession) {
        // 房间集合
        String meetingId = userSession.getMeetingId();
        if (StringUtils.isNotEmpty(meetingId)) {
            // 会议室用户信息
            getMeetingClientList(meetingId);

            // 会议室列表信息
            getServerMeetingStatistics();
        }
    }

    /**
     * 加入房间
     *
     * @param userSession
     */
    @Override
    public void meetingValidate(UserSession userSession) {
        synchronized (userSessions) {
            String meetingId = userSession.getMeetingId();
            // 当前房间用户
            List<UserSession> meetingUserSession = getMeetingUserSessionByMeetingId(userSession.getMeetingId());

            // 房间缓存集合
            List<MeetingStatisticsResDTO> cacheMeeting = (List<MeetingStatisticsResDTO>) redisTemplate.opsForValue().get(CacheKey.MEETING_OBJ_CACHE);
            if (CollUtil.isEmpty(cacheMeeting)) {
                cacheMeeting = new ArrayList<>();
            }

            // 当前房间
            MeetingStatisticsResDTO thisMeeting = null;
            for (MeetingStatisticsResDTO o : cacheMeeting) {
                if (o.getMeetingId().equals(meetingId)) {
                    thisMeeting = o;
                }
            }

            // 查询是否人数超过房间上限
            if ((thisMeeting == null && meetingUserSession.size() > 8) || (thisMeeting != null && meetingUserSession.size() > thisMeeting.getMaxClient())) {
                userSession.setMeetingId(null);
                meetingClientOut(userSession);
                return;
            }

            //查询密码
            if (thisMeeting == null || !thisMeeting.getIsPassword()) {
                return;
            }
            // 房间是否有密码
            if (StringUtils.isEmpty(userSession.getMeetingPassword()) && StringUtils.isNotEmpty(thisMeeting.getPassword())) {
                userSession.setMeetingId(null);
                meetingPasswordError(userSession, OnlineException.MEETING_HAS_PASSWORD.getMsg());
            } else if (!userSession.getMeetingPassword().equals(thisMeeting.getPassword())) {
                userSession.setMeetingId(null);
                meetingPasswordError(userSession, OnlineException.MEETING_PASSWORD_ERR.getMsg());
            } else if (userSession.getMeetingPassword().equals(thisMeeting.getPassword())) {
                meetingPasswordSuccess(userSession);
            }
        }
    }

    /**
     * 加入视频
     *
     * @param userSession
     */
    @Override
    public void joinVideo(UserSession userSession) {
        // 房间集合
        List<Object> conns = new ArrayList<>();
        String meetingId = userSession.getMeetingId();
        if (StringUtils.isNotEmpty(meetingId)) {
            List<UserSession> meetingUserSession = getMeetingUserSessionByMeetingId(meetingId);
            for (UserSession u : meetingUserSession) {
                if (u.getUserId().equals(userSession.getUserId())) {
                    continue;
                }
                HashMap<String, Object> conn = new HashMap<>(2);
                conn.put("id", u.getUserId());
                conn.put("name", u.getName());
                conns.add(conn);

                HashMap<String, Object> data = new HashMap<>();
                data.put("id", userSession.getUserId());
                data.put("name", userSession.getName());
                JsonEntity entity = JsonEntity.builder().eventName(EventName._new_peer).data(data).build();
                entity.sendResMsg(u);
            }

            // 发送人员集合
            HashMap<String, Object> data = new HashMap<>();
            data.put("connections", conns);
            data.put("you", userSession.getUserId());
            JsonEntity entity = JsonEntity.builder().eventName(EventName._peers).data(data).build();
            entity.sendResMsg(userSession);
        }
    }

    /**
     * 加入大厅
     */
    @Override
    public void joinHall() {
        getHallClientList();
        getServerMeetingStatistics();
    }

    /**
     * 验证房间密码成功
     *
     * @param userSession
     */
    private void meetingPasswordSuccess(UserSession userSession) {
        HashMap<String, Object> data = new HashMap<>(1);
        data.put("msg", "加入房间成功!");
        JsonEntity entity = JsonEntity.builder().eventName(EventName.NOTICE).clientFunc(ClientType.meeting_password_success).data(data).build();
        entity.sendResMsg(userSession);
    }

    /**
     * 验证房间密码失败
     *
     * @param userSession
     */
    private void meetingPasswordError(UserSession userSession, String msg) {
        HashMap<String, Object> data = new HashMap<>(1);
        data.put("msg", msg);
        JsonEntity entity = JsonEntity.builder().eventName(EventName.NOTICE).clientFunc(ClientType.meeting_password_error).data(data).build();
        entity.sendResMsg(userSession);
    }

    /**
     * 房间人数达到上限
     *
     * @param userSession
     */
    private void meetingClientOut(UserSession userSession) {
        HashMap<String, Object> data = new HashMap<>(1);
        data.put("msg", OnlineException.MEETING_CLIENT_MAX_OUT.getMsg());
        JsonEntity entity = JsonEntity.builder().eventName(EventName.NOTICE).clientFunc(ClientType.meeting_client_out).data(data).build();
        entity.sendResMsg(userSession);
    }

    @Override
    public void joinSuccess(UserSession userSession) {
        HashMap<String, Object> data = getMeetingInfo(userSession);
        JsonEntity entity = JsonEntity.builder().eventName(EventName.NOTICE).clientFunc(ClientType.join_success).data(data).build();
        entity.sendResMsg(userSession);
    }

    @Override
    public void joinError(UserSession userSession, String message) {
        HashMap<String, Object> data = new HashMap<>(1);
        data.put("msg", message);
        JsonEntity entity = JsonEntity.builder().eventName(EventName.NOTICE).clientFunc(ClientType.join_error).data(data).build();
        entity.sendResMsg(userSession);
    }

    /**
     * 用户退出
     *
     * @param userSession
     */
    @Override
    public void removeSocket(UserSession userSession) {
        getMeetingClientList(userSession.getMeetingId());
//        getHallClientList();
        getServerMeetingStatistics();
    }

    /**
     * 提供offer
     *
     * @param userSession
     * @param data
     */
    @Override
    public void doOffer(UserSession userSession, Map<String, Object> data) {
        // 获取回应socket
        String targetSocketId = String.valueOf(data.get("socketId"));
        String sdp = String.valueOf(data.get("sdp"));
        UserSession targetSession = getSessionByUserId(targetSocketId);


        HashMap<String, Object> json = new HashMap<>(2);
        json.put("socketId", userSession.getUserId());
        json.put("sdp", JSONObject.parse(sdp));
        JsonEntity entity = JsonEntity.builder().eventName(EventName._offer).data(json).build();
        entity.sendResMsg(targetSession);
    }

    /**
     * 提供answer
     *
     * @param userSession
     * @param data
     */
    @Override
    public void doAnswer(UserSession userSession, Map<String, Object> data) {
        // 获取回应socket
        String targetSocketId = String.valueOf(data.get("socketId"));
        String sdp = String.valueOf(data.get("sdp"));
        UserSession targetSession = getSessionByUserId(targetSocketId);


        HashMap<String, Object> json = new HashMap<>(2);
        json.put("socketId", userSession.getUserId());
        json.put("sdp", JSONObject.parse(sdp));
        JsonEntity entity = JsonEntity.builder().eventName(EventName._answer).data(json).build();
        entity.sendResMsg(targetSession);
    }

    /**
     * 提供ice_candidate
     *
     * @param userSession
     * @param data
     */
    @Override
    public void doIce(UserSession userSession, Map<String, Object> data) {
        // 获取回应socket
        String targetSocketId = String.valueOf(data.get("socketId"));
        String id = String.valueOf(data.get("id"));
        String label = String.valueOf(data.get("label"));
        String socketId = userSession.getUserId();
        String candidate = String.valueOf(data.get("candidate"));
        String candidatePC = String.valueOf(data.get("candidatePC"));
        UserSession targetSession = getSessionByUserId(targetSocketId);

        HashMap<String, Object> json = new HashMap<>(4);
        json.put("id", id);
        json.put("label", label);
        json.put("socketId", socketId);
        json.put("candidate", candidate);
        json.put("candidatePC", JSONObject.parse(candidatePC));
        log.info("发送返回给用户:" + targetSocketId + ",他需要去认证用户" + socketId);
        JsonEntity entity = JsonEntity.builder().eventName(EventName._ice_candidate).data(json).build();
        entity.sendResMsg(targetSession);

        log.info("发送完毕！");
    }

    /**
     * 获取所有会议室
     *
     * @return
     */
    @Override
    public List<MeetingStatisticsResDTO> getAllMeeting() {
        List<String> meetingIds = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        for (UserSession u : this.userSessions.values()) {
            if (StringUtils.isNotEmpty(u.getMeetingId())) {
                if (!meetingIds.contains(u.getMeetingId())) {
                    map.put(u.getMeetingId(), 1);
                    meetingIds.add(u.getMeetingId());
                } else {
                    map.put(u.getMeetingId(), map.get(u.getMeetingId()) + 1);
                }
            }
        }

        // 查询已存在的房间信息
        if (CollUtil.isEmpty(meetingIds)) {
            return new ArrayList<>();
        }
        List<MeetingStatisticsResDTO> res = new ArrayList<>();
        List<MeetingStatisticsResDTO> cacheMeeting;
        try {
            cacheMeeting = (List<MeetingStatisticsResDTO>) redisTemplate.opsForValue().get(CacheKey.MEETING_OBJ_CACHE);
        } catch (Exception e) {
            e.printStackTrace();
            throw BizException.wrap("获取缓存错误!");
        }
        if (CollUtil.isNotEmpty(cacheMeeting)) {
            for (MeetingStatisticsResDTO o : cacheMeeting) {
                if (meetingIds.contains(o.getMeetingId())) {
                    res.add(MeetingStatisticsResDTO.builder()
                            .meetingId(o.getMeetingId())
                            .meetingName(o.getMeetingName())
                            .maxClient(o.getMaxClient())
                            .owner(o.getOwner())
                            .isPassword(o.getIsPassword())
                            .descs(o.getDescs())
                            .build());
                }
            }
        }
        List<String> collect = res.stream().map(MeetingStatisticsResDTO::getMeetingId).collect(Collectors.toList());

        // 添加上临时会议
        for (String meetingId : meetingIds) {
            if (!collect.contains(meetingId)) {
                res.add(MeetingStatisticsResDTO.builder()
                        .meetingId(meetingId)
                        .meetingName("临时会议-" + meetingId)
                        .build());
            }
        }

        for (MeetingStatisticsResDTO o : res) {
            o.setNum(map.get(o.getMeetingId()));
        }

        return res;
    }

    /**
     * 获取某个房间里的所有人
     *
     * @param meetingId
     * @return
     */
    private List<UserSession> getMeetingUserSessionByMeetingId(String meetingId) {
        List<UserSession> sessions = new ArrayList<>();
        for (UserSession u : userSessions.values()) {
            if (u.getMeetingId() != null && u.getMeetingId().equals(meetingId)) {
                sessions.add(u);
            }
        }
        return sessions;
    }

    /**
     * 获取大厅里的所有人
     *
     * @return
     */
    private List<UserSession> getMeetingUserSessionByHall() {
        List<UserSession> sessions = new ArrayList<>();
        for (UserSession u : userSessions.values()) {
            if (StringUtils.isEmpty(u.getMeetingId())) {
                sessions.add(u);
            }
        }
        return sessions;
    }

    /**
     * 根据socketId获取session
     *
     * @param socketId
     * @return
     */
    private UserSession getSessionBySocketId(String socketId) {
        for (UserSession u : userSessions.values()) {
            if (socketId.equals(u.getSession().getId())) {
                return u;
            }
        }
        return null;
    }

    /**
     * 根据userId获取session
     *
     * @param userId
     * @return
     */
    private UserSession getSessionByUserId(String userId) {
        for (UserSession u : userSessions.values()) {
            if (userId.equals(u.getUserId())) {
                return u;
            }
        }

        throw BizException.wrap("没有找到该用户session!");
    }

    /**
     * 通知-获取某会议客户端列表
     *
     * @param meetingId
     */
    private void getMeetingClientList(String meetingId) {
        List<Object> meetingClients = new ArrayList<>();

        if (StringUtils.isNotEmpty(meetingId)) {
            List<UserSession> meetingUserSession = getMeetingUserSessionByMeetingId(meetingId);
            for (UserSession u : meetingUserSession) {
                HashMap<String, Object> json = new HashMap<>(3);
                json.put("userId", u.getUserId());
                json.put("userName", u.getName());
                json.put("userPhoto", u.getPhoto());
                meetingClients.add(json);
            }

            // 向该会议室所有人发送会议室人数情况
            for (UserSession u : meetingUserSession) {
                HashMap<String, Object> json = new HashMap<>(1);
                // 设置会议的客户端方法
                json.put("obj", meetingClients);
                JsonEntity entity = JsonEntity.builder().eventName(EventName.NOTICE).clientFunc(ClientType.meetingClients).data(json).build();
                entity.sendResMsg(u);
            }
        }

        getHallClientList();
    }

    /**
     * 通知-获取大厅客户端列表
     */
    private void getHallClientList() {
        List<Object> sendData = new ArrayList<>();
        List<UserSession> tempUserSession = getMeetingUserSessionByHall();
        for (UserSession u : tempUserSession) {
            HashMap<String, Object> json = new HashMap<>(3);
            json.put("userId", u.getUserId());
            json.put("userName", u.getName());
            json.put("userPhoto", u.getPhoto());
            sendData.add(json);
        }

        // 向所有用户发送大厅情况
        for (UserSession u : userSessions.values()) {
            HashMap<String, Object> json = new HashMap<>(1);
            // 设置大厅的客户端方法
            json.put("obj", sendData);
            JsonEntity entity = JsonEntity.builder().eventName(EventName.NOTICE).clientFunc(ClientType.hallClients).data(json).build();
            entity.sendResMsg(u);
        }
    }

    /**
     * 通知-获取所有会议室
     */
    private void getServerMeetingStatistics() {
        List<Object> sendData = new ArrayList<>();
        List<MeetingStatisticsResDTO> meetings = getAllMeeting();
        for (MeetingStatisticsResDTO o : meetings) {
            HashMap<String, Object> json = new HashMap<>(3);
            json.put("meeting", o);
            json.put("length", o.getNum() + "/" + o.getMaxClient());
            json.put("type", "serverMeetingStatistics");
            sendData.add(json);
        }

        // 向所有用户发送会议室情况
        for (UserSession u : userSessions.values()) {
            HashMap<String, Object> json = new HashMap<>(1);
            // 设置大厅的客户端方法
            json.put("obj", sendData);
            JsonEntity entity = JsonEntity.builder().eventName(EventName.NOTICE).clientFunc(ClientType.serverMeetingStatistics).data(json).build();
            entity.sendResMsg(u);
        }
    }

    /**
     * 通知-获取所有会议室
     */
    private HashMap<String, Object> getMeetingInfo(UserSession session) {
        HashMap<String, Object> data = new HashMap<>();
        if (StringUtils.isEmpty(session.getMeetingId())) {
            return data;
        }
        List<MeetingStatisticsResDTO> cacheMeeting = (List<MeetingStatisticsResDTO>) redisTemplate.opsForValue().get(CacheKey.MEETING_OBJ_CACHE);
        if (CollUtil.isNotEmpty(cacheMeeting)) {
            cacheMeeting.forEach(o -> {
                if (o.getMeetingId().equals(session.getMeetingId())) {
                    data.put("meetingId", o.getMeetingId());
                    data.put("meetingName", o.getMeetingName());
                    data.put("type", "normal");
                    data.put("desc", o.getDescs());
                }
            });

            if (data.size() == 0) {
                data.put("meetingId", session.getMeetingId());
                data.put("meetingName", "临时会议" + session.getMeetingId());
                data.put("type", "tmp");
                data.put("desc", "这是一个临时房间");
            }
        }
        return data;
    }

    /**
     * 执行方法
     */
    @Override
    public void invokeMethod(UserSession userSession, JsonEntity jsonEntity) {
        //截取实例名称;
        String funcBean = "onlineAutoService";
        if (jsonEntity.getServerFunc() == null) {
            throw BizException.wrap("通用通知报错:不存在通知子项服务类型");
        }
        String funName = jsonEntity.getServerFunc().getCode();

        Object bean = SpringUtils.getBean(funcBean);
        Method method = ReflectionUtils.findMethod(bean.getClass(), funName, UserSession.class, JsonEntity.class);
        if (method != null) {
            ReflectionUtils.invokeMethod(method, bean, userSession, jsonEntity);
        }
    }

    /**
     * 异常关闭socket
     *
     * @param session
     */
    @Override
    public void close(WebSocketSession session, String msg) {
        try {
            session.close();
            log.info(msg);
            throw BizException.wrap(msg);
        } catch (IOException ignored) {
            msg = "出现不可忽略异常:" + msg;
            log.info(msg);
            throw BizException.wrap(msg);
        }
    }
}
