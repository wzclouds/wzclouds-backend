package com.github.wzclouds.online.controller;

import com.github.wzclouds.base.R;
import com.github.wzclouds.base.controller.BaseController;
import com.github.wzclouds.constant.FileUploadType;
import com.github.wzclouds.dozer.DozerUtils;
import com.github.wzclouds.exception.BizException;
import com.github.wzclouds.online.dto.FileEnResDTO;
import com.github.wzclouds.online.dto.MeetingStatisticsResDTO;
import com.github.wzclouds.online.dto.MeetingUseInfoReqDTO;
import com.github.wzclouds.online.entity.BizFile;
import com.github.wzclouds.online.service.BizFileService;
import com.github.wzclouds.online.service.OnlineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xnio.Result;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * <p>
 * </p>
 *
 * @author wz
 * @date 2020-11-19
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/anno/")
@Api(value = "anno", tags = "对外接口")
public class AnnoController {
    @Autowired
    private OnlineService onlineService;

    @Autowired
    private BizFileService bizFileService;

    @Autowired
    private DozerUtils dozerUtils;

    @ApiOperation(value = "上传白板绘制图片", notes = "上传白板绘制图片")
    @RequestMapping(value = "/uploadExtImg", method = RequestMethod.POST)
    public R<FileEnResDTO> upload(@RequestParam(value = "file") MultipartFile file,
                                       @RequestParam(value = "userId") String userId,
                                       @RequestParam(value = "meetingId") String meetingId) {
        if (file == null) {
            return R.fail("请上传文件");
        }
        BizFile po = bizFileService.save(file, FileUploadType.SHARP_IMG, userId, null, meetingId);
        FileInputStream fileInputStream = null;
        try {
            File tempFile = new File(po.getFilePath());
            fileInputStream = new FileInputStream(tempFile);
            BufferedImage bufferedImage = ImageIO.read(fileInputStream);
            FileEnResDTO res = dozerUtils.map(po, FileEnResDTO.class);
            res.setHeight(bufferedImage.getHeight());
            res.setWidth(bufferedImage.getWidth());
            fileInputStream.close();
            return R.success(res);
        } catch (IOException e) {
            throw BizException.wrap(e.getMessage());
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @ApiOperation(value = "查询正在使用的会议", notes = "查询正在使用的会议")
    @PostMapping("findUsedMeeting")
    public R<List<MeetingStatisticsResDTO>> findUsedMeeting(@RequestBody MeetingUseInfoReqDTO dto) {
        List<MeetingStatisticsResDTO> meetings = onlineService.getAllMeeting();
        if (StringUtils.isNotEmpty(dto.getMeetingName())) {
            meetings = meetings.stream().filter(o -> o.getMeetingName().contains(dto.getMeetingName())).collect(Collectors.toList());
        }

        return R.success(meetings);
    }
}