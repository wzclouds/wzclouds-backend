package com.github.wzclouds.base;

import cn.hutool.core.collection.CollUtil;
import com.github.wzclouds.common.UserSession;
import com.github.wzclouds.exception.BizException;
import com.github.wzclouds.online.dto.UserResDTO;
import com.github.wzclouds.online.entity.User;
import com.github.wzclouds.online.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xnio.Result;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "login")
@Api(value = "login", tags = "登录认证相关")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private ConcurrentHashMap<String, UserSession> userSessions;


    @ApiOperation(value = "内部token校验接口", notes = "内部token校验接口")
    @GetMapping(value = "validate")
    public R<UserResDTO> validate(@RequestParam(value = "ticket", required = false) String ticket) {
        if (StringUtils.isEmpty(ticket)) {
            return R.success(userService.getTempUser());
        }
        User user = userService.decode(ticket);
        Collection<UserSession> values = userSessions.values();
        if (CollUtil.isNotEmpty(values)) {
            List<String> userIds = values.stream().map(UserSession::getUserId).collect(Collectors.toList());
            if (userIds.contains(String.valueOf(user.getId()))) {
                return R.fail("该用户已登录，请勿重复登录!");
            }
        }
        UserResDTO res = UserResDTO.builder()
                .userId(String.valueOf(user.getId()))
                .userName(user.getName())
                .type("formal")
                .photo(user.getPhoto())
                .token(ticket)
                .build();
        return R.success(res);
    }

    @ApiOperation(value = "一般登录接口", notes = "一般登录接口")
    @GetMapping(value = "login")
    public R<UserResDTO> login(@RequestParam(value = "account") String account, String password) {
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            throw BizException.wrap("账号或密码不可为空");
        }
        UserResDTO res = userService.encode(account, password);
        return R.success(res);
    }
}
