package com.github.wzclouds.online.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.wzclouds.base.R;
import com.github.wzclouds.base.request.PageParams;
import com.github.wzclouds.exception.BizException;
import com.github.wzclouds.online.dto.MeetingPageReqDTO;
import com.github.wzclouds.online.dto.MeetingSaveReqDTO;
import com.github.wzclouds.online.entity.Meeting;
import com.github.wzclouds.online.service.MeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "meeting")
@Api(value = "meeting", tags = "会议室相关")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @ApiOperation(value = "分页查询会议", notes = "分页查询会议")
    @PostMapping("pageMeetingListByUser")
    public R<IPage<Meeting>> pageMeetingListByUser(@RequestBody PageParams<MeetingPageReqDTO> dto) {
        IPage<Meeting> res = meetingService.pageMeetingListByUser(dto);
        return R.success(res);
    }

    @ApiOperation(value = "删除会议", notes = "删除会议")
    @GetMapping("deleteMeeting")
    public R<Boolean> deleteMeeting(@RequestParam(value = "ids[]") Long[] ids) {
        Boolean res = false;
        if (ids != null && ids.length > 0) {
            List<Long> list = Arrays.asList(ids);
            res = meetingService.deleteMeeting(list);
        }
        return R.success(res);
    }

    @ApiOperation(value = "新增会议", notes = "新增会议")
    @PostMapping("saveMeeting")
    public R<Meeting> saveMeeting(@RequestBody MeetingSaveReqDTO data) {
        Meeting res = meetingService.saveMeeting(data);
        return R.success(res);
    }

    @ApiOperation(value = "更新会议", notes = "更新会议")
    @PostMapping("updateMeeting")
    public R<Boolean> updateMeeting(@RequestBody MeetingSaveReqDTO data) {
        if (data.getId() == null) {
            throw BizException.wrap("缺少会议id!");
        }
        if (data.getIspassword() && StringUtils.isEmpty(data.getPassword())) {
            throw BizException.wrap("启用加密功能需要先设置密码!");
        }
        Boolean res = meetingService.updateMeeting(data);
        return R.success(res);
    }
}
