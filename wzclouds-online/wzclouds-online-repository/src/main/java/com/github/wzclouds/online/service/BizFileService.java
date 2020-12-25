package com.github.wzclouds.online.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.wzclouds.base.service.SuperCacheService;
import com.github.wzclouds.constant.FileUploadType;
import com.github.wzclouds.online.dto.BizFileReqDTO;
import com.github.wzclouds.online.dto.BizFileResDTO;
import com.github.wzclouds.online.entity.BizFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 业务接口
 * <p>
 * </p>
 *
 * @author wz
 * @date 2020-11-19
 */
public interface BizFileService extends SuperCacheService<BizFile> {

    BizFile save(MultipartFile file,
                 FileUploadType bizType,
                 String createUser,
                 String bizId,
                 String meetingId);

    List<BizFile> save(List<MultipartFile> files,
                       FileUploadType bizType,
                       String createUser,
                       String bizId,
                       String meetingId);

    IPage<BizFileResDTO> pageBizFile(IPage<BizFile> page, BizFileReqDTO data);
}
