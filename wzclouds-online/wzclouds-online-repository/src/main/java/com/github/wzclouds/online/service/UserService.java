package com.github.wzclouds.online.service;

import com.github.wzclouds.base.service.SuperCacheService;
import com.github.wzclouds.common.UserSession;
import com.github.wzclouds.online.dto.UserResDTO;
import com.github.wzclouds.online.entity.User;

/**
 * <p>
 * 业务接口
 * 用户表
 * </p>
 *
 * @author wz
 * @date 2020-11-03
 */
public interface UserService extends SuperCacheService<User> {

    User decode(String token);

    UserResDTO encode(String account, String password);

    UserResDTO getTempUser();

    void addOnlineCache(User dto);

    void removeOnlineCache(UserSession userSession);
}
