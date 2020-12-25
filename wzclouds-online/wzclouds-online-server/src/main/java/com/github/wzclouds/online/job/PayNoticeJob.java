package com.github.wzclouds.online.job;

import com.github.wzclouds.constant.CacheKey;
import com.github.wzclouds.online.dto.MeetingStatisticsResDTO;
import com.github.wzclouds.online.dto.UserCacheDTO;
import com.github.wzclouds.online.entity.Meeting;
import com.github.wzclouds.online.entity.User;
import com.github.wzclouds.online.service.MeetingService;
import com.github.wzclouds.online.service.UserService;
import com.github.wzclouds.dozer.DozerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ReservationJob
 * @Description: 示例定时
 * @Author: yongchen
 * @Date: 2020/6/12 11:27
 **/
@Slf4j
@Component
@Transactional
public class PayNoticeJob {
    @Autowired
    private MeetingService meetingService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    @Scheduled(cron = "0 0 * * * ?") //每小时执行
    public void cacheMeeting() {
        List<Meeting> list = meetingService.list();
        List<MeetingStatisticsResDTO> res = new ArrayList<>();
        list.forEach(o -> res.add(MeetingStatisticsResDTO
                .builder()
                .meetingId(String.valueOf(o.getId()))
                .meetingName(o.getName())
                .descs(o.getDescs())
                .password(o.getPassword())
                .isPassword(o.getIspassword())
                .owner(o.getOwner())
                .maxClient(o.getMaxClient())
        .build()));
        redisTemplate.opsForValue().set(CacheKey.MEETING_OBJ_CACHE, res);
        redisTemplate.expire(CacheKey.MEETING_OBJ_CACHE, CacheKey.CACHE_EXPIRE, TimeUnit.SECONDS);
        log.info("执行会议室信息缓存！");
    }

    @PostConstruct
    @Scheduled(cron = "0 0 * * * ?")
    public void cacheUser() {
        List<User> list = userService.list();
        List<UserCacheDTO> res = new ArrayList<>();
        list.forEach(o -> res.add(UserCacheDTO
                .builder()
                .id(o.getId())
                .account(o.getAccount())
                .name(o.getName())
                .outUserId(o.getOutUserId())
                .build()));
        redisTemplate.opsForValue().set(CacheKey.USER_OBJ_CACHE, res);
        redisTemplate.expire(CacheKey.USER_OBJ_CACHE, CacheKey.CACHE_EXPIRE, TimeUnit.SECONDS);
        log.info("执行用户信息缓存！");
    }
}
