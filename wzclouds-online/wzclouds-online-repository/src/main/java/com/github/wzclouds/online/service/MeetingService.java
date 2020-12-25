package com.github.wzclouds.online.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.wzclouds.base.request.PageParams;
import com.github.wzclouds.base.service.SuperCacheService;
import com.github.wzclouds.online.dto.MeetingPageReqDTO;
import com.github.wzclouds.online.dto.MeetingSaveReqDTO;
import com.github.wzclouds.online.entity.Meeting;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 
 * </p>
 *
 * @author wz
 * @date 2020-11-03
 */
public interface MeetingService extends SuperCacheService<Meeting> {

    IPage<Meeting> pageMeetingListByUser(PageParams<MeetingPageReqDTO> dto);

    boolean deleteMeeting(List<Long> list);

    Meeting saveMeeting(MeetingSaveReqDTO data);

    Boolean updateMeeting(MeetingSaveReqDTO data);
}
