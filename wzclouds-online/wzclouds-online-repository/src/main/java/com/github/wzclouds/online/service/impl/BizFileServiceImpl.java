package com.github.wzclouds.online.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.wzclouds.base.service.SuperCacheServiceImpl;
import com.github.wzclouds.constant.CacheKey;
import com.github.wzclouds.constant.FileServerProperties;
import com.github.wzclouds.constant.FileUploadType;
import com.github.wzclouds.context.BaseContextHandler;
import com.github.wzclouds.database.mybatis.conditions.Wraps;
import com.github.wzclouds.database.mybatis.conditions.query.LbqWrapper;
import com.github.wzclouds.exception.BizException;
import com.github.wzclouds.online.dao.BizFileMapper;
import com.github.wzclouds.online.dto.BizFileReqDTO;
import com.github.wzclouds.online.dto.BizFileResDTO;
import com.github.wzclouds.online.dto.UserCacheDTO;
import com.github.wzclouds.online.entity.BizFile;
import com.github.wzclouds.online.service.BizFileService;
import com.github.wzclouds.utils.BeanPlusUtil;
import com.github.wzclouds.dozer.DozerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 业务实现类
 * <p>
 * </p>
 *
 * @author wz
 * @date 2020-11-19
 */
@Slf4j
@Service
public class BizFileServiceImpl extends SuperCacheServiceImpl<BizFileMapper, BizFile> implements BizFileService {
    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private DozerUtils dozerUtils;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private FileServerProperties fileServerProperties;

    @Override
    protected String getRegion() {
        return "bizFile";
    }

    @Override
    public BizFile save(MultipartFile file,
                        FileUploadType bizType,
                        String createUser,
                        String bizId,
                        String meetingId) {
        if (file == null) {
            throw BizException.wrap("请上传文件");
        }
        String uploadPath = getUploadFilePath(file);
        BizFile po = BizFile.builder()
                .id(String.valueOf(uidGenerator.getUID()))
                .createUser(createUser)
                .filePath(uploadPath)
                .name(file.getOriginalFilename())
                .url(fileServerProperties.getUriPrefix() + getDataUploadPath() + file.getOriginalFilename())
                .type(file.getContentType())
                .bizId(bizId)
                .bizType(bizType)
                .meetingId(meetingId)
                .build();
        File local = new File(getUploadPath());
        if (!local.exists()) {
            local.mkdirs();
        }
        try {
            File temp = new File(uploadPath);
            if (!temp.isFile()) {
                file.transferTo(temp);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw BizException.wrap("上传文件失败!");
        }
        super.save(po);
        return po;
    }

    @Override
    public List<BizFile> save(List<MultipartFile> files,
                              FileUploadType bizType,
                              String createUser,
                              String bizId,
                              String meetingId) {
        if (CollUtil.isEmpty(files)) {
            throw BizException.wrap("请上传文件");
        }
        List<BizFile> res = new ArrayList<>();
        files.forEach(file -> {
            String uploadPath = getUploadFilePath(file);
            BizFile po = BizFile.builder()
                    .id(String.valueOf(uidGenerator.getUID()))
                    .createUser(createUser)
                    .filePath(uploadPath)
                    .name(file.getOriginalFilename())
                    .url(fileServerProperties.getUriPrefix() + getDataUploadPath() + file.getOriginalFilename())
                    .type(file.getContentType())
                    .bizId(bizId)
                    .bizType(bizType)
                    .meetingId(meetingId)
                    .build();
            File local = new File(getUploadPath());
            if (!local.exists()) {
                local.mkdirs();
            }
            try {
                File temp = new File(uploadPath);
                if (!temp.isFile()) {
                    file.transferTo(temp);
                }
            } catch (IOException e) {
                log.error(e.getMessage());
                throw BizException.wrap("上传文件失败!");
            }
            res.add(po);
        });
        super.saveBatch(res);
        return res;
    }

    @Override
    public IPage<BizFileResDTO> pageBizFile(IPage<BizFile> page, BizFileReqDTO data) {
        LbqWrapper<BizFile> wrapper = Wraps.lbQ();
        wrapper.eq(BizFile::getCreateUser, BaseContextHandler.getUserId())
                .like(BizFile::getName, data.getName())
                .eq(BizFile::getBizType, FileUploadType.BIZ_FILE)
                .eq(BizFile::getBizId, data.getBizId())
                .eq(BizFile::getMeetingId, data.getMeetingId())
                .orderByDesc(BizFile::getCreateTime);

        IPage<BizFileResDTO> res = BeanPlusUtil.toBeanPage(page(page, wrapper), BizFileResDTO.class);
        if (CollUtil.isNotEmpty(res.getRecords())) {
            List<UserCacheDTO> users = (List<UserCacheDTO>) redisTemplate.opsForValue().get(CacheKey.USER_OBJ_CACHE);
            if (CollUtil.isEmpty(users)) {
                users = new ArrayList<>();
            }
            List<UserCacheDTO> finalUsers = users;
            res.getRecords().forEach(obj -> {
                if (obj.getBizId().startsWith("tmp")) {
                    obj.setUserName("临时用户" + obj.getBizId().replace("tmp", ""));
                } else {
                    finalUsers.forEach(user -> {
                        if (String.valueOf(user.getId()).equals(obj.getBizId())) {
                            obj.setUserName(user.getName());
                        }
                    });
                }
            });
        }
        return res;
    }

    private String getUploadPath(){
        return fileServerProperties.getStoragePath() + "/" + LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue();
    }

    private String getDataUploadPath(){
        return LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue() + "/";
    }

    private String getUploadFilePath(MultipartFile file){
        return fileServerProperties.getStoragePath() + "/" + LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue() + "/" + file.getOriginalFilename();
    }
}
