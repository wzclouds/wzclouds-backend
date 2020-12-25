package com.github.wzclouds.base;

import cn.hutool.crypto.SecureUtil;
import com.github.wzclouds.context.BaseContextHandler;
import com.github.wzclouds.exception.BizException;
import com.github.wzclouds.online.dto.UserPasswordUpdateDTO;
import com.github.wzclouds.online.entity.User;
import com.github.wzclouds.online.service.UserService;
import com.github.wzclouds.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "user")
@Api(value = "用户", tags = "用户信息相关")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @PostMapping(value = "updatePassword")
    public R<Boolean> updatePassword(@RequestBody UserPasswordUpdateDTO dto) {
        Long userId = BaseContextHandler.getUserId();
        User user = userService.getById(userId);
        if (user == null) {
            throw BizException.wrap("未找到用户!");
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw BizException.wrap("确认密码与新密码不一致，请重新输入!");
        }

        String oldPassword = MD5Utils.getPassMD5(dto.getOldPassword().toLowerCase());
        if (!user.getPassword().equals(oldPassword)) {
            throw BizException.wrap("密码错误，请重新输入!");
        }

        user.setPassword(MD5Utils.getPassMD5(dto.getNewPassword()).toLowerCase());
        return R.success(userService.updateById(user));
    }
}
