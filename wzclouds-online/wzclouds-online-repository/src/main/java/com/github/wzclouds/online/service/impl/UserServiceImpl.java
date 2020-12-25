package com.github.wzclouds.online.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.github.wzclouds.base.service.SuperCacheServiceImpl;
import com.github.wzclouds.common.UserSession;
import com.github.wzclouds.database.mybatis.conditions.Wraps;
import com.github.wzclouds.database.mybatis.conditions.query.LbqWrapper;
import com.github.wzclouds.exception.BizException;
import com.github.wzclouds.jwt.TokenUtil;
import com.github.wzclouds.jwt.model.AuthInfo;
import com.github.wzclouds.jwt.model.JwtUserInfo;
import com.github.wzclouds.online.dao.UserMapper;
import com.github.wzclouds.online.dto.UserResDTO;
import com.github.wzclouds.online.entity.User;
import com.github.wzclouds.online.enumeration.UserType;
import com.github.wzclouds.online.service.OnlineService;
import com.github.wzclouds.online.service.UserService;
import com.github.wzclouds.utils.EventName;
import com.github.wzclouds.utils.JsonEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 业务实现类
 * 用户表
 * </p>
 *
 * @author wz
 * @date 2020-11-03
 */
@Slf4j
@Service
public class UserServiceImpl extends SuperCacheServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private ConcurrentHashMap<String, UserSession> userSessions;

    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private OnlineService onlineService;

    @Override
    protected String getRegion() {
        return "user";
    }

    @Override
    public User decode(String token) {
        AuthInfo info = tokenUtil.getAuthInfo(token);
        Long userId = info.getUserId();
        User user = getById(userId);
        if (user == null) {
            throw BizException.wrap("未找到该用户!");
        }
        return user;
    }

    @Override
    public UserResDTO encode(String account, String password) {
        LbqWrapper<User> wrapper = Wraps.lbQ();
        wrapper.eq(User::getAccount, account)
                .eq(User::getPassword, SecureUtil.md5(password));
        User user = getOne(wrapper, false);
        if (user == null) {
            throw BizException.wrap("账号或密码错误!");
        }
        return getUserTokenById(user);
    }

    @Override
    public UserResDTO getTempUser() {
        String userId = String.valueOf(uidGenerator.getUID());
        UserResDTO res = UserResDTO.builder()
                .userId("tmp" + userId)
                .userName("临时用户" + userId)
                .type("tmp")
                .photo("/cloud/meeting-static/lib/img/39.png")
                .token("tmp" + userId)
                .build();
        return res;
    }

    @Override
    public void addOnlineCache(User dto) {

    }

    @Override
    public void removeOnlineCache(UserSession userSession) {
        if (this.userSessions == null) {
            return;
        }
        userSessions.remove(userSession.getSession().getId());
        for (UserSession u : this.userSessions.values()) {
            if (StringUtils.isNotEmpty(u.getMeetingId()) && u.getMeetingId().equals(userSession.getMeetingId())) {
                HashMap<String, Object> json = new HashMap<>(1);
                json.put("socketId", userSession.getUserId());
                log.info("通知用户" + u.getName() + " : " + userSession.getName() + "退出");
                JsonEntity entity = JsonEntity.builder().eventName(EventName._remove_peer).data(json).build();
                entity.sendResMsg(u);
            }
        }
        onlineService.removeSocket(userSession);
    }

    private UserResDTO getUserTokenById(User user) {
        UserResDTO res = new UserResDTO();
        JwtUserInfo userInfo = new JwtUserInfo(user.getId(), user.getAccount(), user.getName());

        AuthInfo authInfo = tokenUtil.createAuthInfo(userInfo, null);
        res.setUserId(String.valueOf(user.getId()));
        res.setUserName(String.valueOf(user.getName()));
        res.setPhoto(String.valueOf(user.getPhoto()));
        res.setType(String.valueOf(user.getType().getCode()));

        res.setToken(authInfo.getToken());
        res.setRefreshToken(authInfo.getRefreshToken());
        return res;
    }
}
