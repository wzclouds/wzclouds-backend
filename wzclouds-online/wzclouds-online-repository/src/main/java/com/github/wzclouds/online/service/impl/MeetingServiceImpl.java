package com.github.wzclouds.online.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.wzclouds.base.request.PageParams;
import com.github.wzclouds.base.service.SuperCacheServiceImpl;
import com.github.wzclouds.context.BaseContextHandler;
import com.github.wzclouds.database.mybatis.conditions.Wraps;
import com.github.wzclouds.database.mybatis.conditions.query.LbqWrapper;
import com.github.wzclouds.exception.BizException;
import com.github.wzclouds.online.dao.MeetingMapper;
import com.github.wzclouds.online.dto.MeetingPageReqDTO;
import com.github.wzclouds.online.dto.MeetingSaveReqDTO;
import com.github.wzclouds.online.entity.Meeting;
import com.github.wzclouds.online.service.MeetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 业务实现类
 * 
 * </p>
 *
 * @author wz
 * @date 2020-11-03
 */
@Slf4j
@Service
public class MeetingServiceImpl extends SuperCacheServiceImpl<MeetingMapper, Meeting> implements MeetingService {
    @Override
    protected String getRegion() {
        return "meeting";
    }

    @Override
    public IPage<Meeting> pageMeetingListByUser(PageParams<MeetingPageReqDTO> dto) {
        // 创建人
        Long accountId = BaseContextHandler.getUserId();
        MeetingPageReqDTO data = dto.getModel();
        IPage page = dto.buildPage();
        LbqWrapper<Meeting> wrapper = Wraps.lbQ();
        wrapper.like(Meeting::getName, data.getName())
                .eq(Meeting::getOwner, accountId)
                .orderByDesc(Meeting::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public boolean deleteMeeting(List<Long> list) {
        return removeByIds(list);
    }

    @Override
    public Meeting saveMeeting(MeetingSaveReqDTO data) {
        save(data);
        return data;
    }

    @Override
    public Boolean updateMeeting(MeetingSaveReqDTO data) {
        Meeting meeting = getById(data.getId());
        if (meeting == null) {
            throw BizException.wrap("未找到该会议!");
        }
        return updateById(data);
    }
}
