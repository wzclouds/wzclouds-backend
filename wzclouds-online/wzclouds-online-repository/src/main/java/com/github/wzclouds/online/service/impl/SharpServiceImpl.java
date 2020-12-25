package com.github.wzclouds.online.service.impl;

import com.github.wzclouds.base.service.SuperCacheServiceImpl;
import com.github.wzclouds.online.dao.SharpMapper;
import com.github.wzclouds.online.entity.Sharp;
import com.github.wzclouds.online.service.SharpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务实现类
 * 
 * </p>
 *
 * @author wz
 * @date 2020-11-19
 */
@Slf4j
@Service
public class SharpServiceImpl extends SuperCacheServiceImpl<SharpMapper, Sharp> implements SharpService {

    @Override
    protected String getRegion() {
        return "sharp";
    }
}
