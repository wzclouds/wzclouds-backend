package com.github.wzclouds.online.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.wzclouds.base.R;
import com.github.wzclouds.base.request.PageParams;
import com.github.wzclouds.constant.FileUploadType;
import com.github.wzclouds.context.BaseContextHandler;
import com.github.wzclouds.dozer.DozerUtils;
import com.github.wzclouds.exception.BizException;
import com.github.wzclouds.online.dto.BizFileReqDTO;
import com.github.wzclouds.online.dto.BizFileResDTO;
import com.github.wzclouds.online.dto.FileEnResDTO;
import com.github.wzclouds.online.entity.BizFile;
import com.github.wzclouds.online.service.BizFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

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
@RequestMapping("/bizFile")
@Api(value = "BizFile", tags = "文件上传")
public class BizFileController {

    @Autowired
    private BizFileService bizFileService;

    @Autowired
    private DozerUtils dozerUtils;

    @PostMapping(value = "/uploadBiz")
    @ApiOperation(value = "业务文件上传", notes = "业务文件上传")
    public R<FileEnResDTO> upload(@RequestParam(value = "file") MultipartFile file,
                                  @RequestParam(value = "bizType") FileUploadType fileUploadType,
                                  @RequestParam(value = "bizId", required = false) String bizId,
                                  @RequestParam(value = "meetingId") String meetingId) {
        if (file == null) {
            return R.fail("请上传文件");
        }
        BizFile po = bizFileService.save(file, fileUploadType, BaseContextHandler.getUserIdStr(), bizId, meetingId);
        FileEnResDTO res = dozerUtils.map(po, FileEnResDTO.class);
        return R.success(res);
    }

    @PostMapping(value = "/uploadBizs")
    @ApiOperation(value = "业务文件集合上传", notes = "业务文件集合上传")
    public R<List<FileEnResDTO>> upload(@RequestParam(value = "file") List<MultipartFile> file,
                                        @RequestParam(value = "bizType") FileUploadType fileUploadType,
                                        @RequestParam(value = "bizId", required = false) String bizId,
                                        @RequestParam(value = "meetingId") String meetingId) {
        if (file == null) {
            return R.fail("请上传文件");
        }
        List<BizFile> po = bizFileService.save(file, fileUploadType, BaseContextHandler.getUserIdStr(), bizId, meetingId);
        List<FileEnResDTO> res = dozerUtils.mapList(po, FileEnResDTO.class);
        return R.success(res);
    }

    /**
     * 分页查找业务文件
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/pageBizFile")
    @ApiOperation(value = "分页查找业务文件", notes = "分页查找业务文件")
    public R<IPage<BizFileResDTO>> pageFile(@RequestBody PageParams<BizFileReqDTO> dto) {
        BizFileReqDTO data = dto.getModel();
        IPage page = dto.buildPage();

        IPage<BizFileResDTO> res = bizFileService.pageBizFile(page, data);
        return R.success(res);
    }

    /**
     * 批量删除业务文件
     *
     * @param ids
     * @return
     */
    @GetMapping(value = "/removeFiles")
    @ApiOperation(value = "批量删除业务文件", notes = "批量删除业务文件")
    public R<Boolean> removeFiles(@RequestParam(value = "ids[]") Long[] ids) {
        boolean res = bizFileService.removeByIds(Arrays.asList(ids));
        return R.success(res);
    }

    /**
     * 下载业务文件
     *
     * @param response
     * @param id
     */
    @GetMapping("/downLoad")
    @ApiOperation(value = "下载业务文件", notes = "下载业务文件")
    public void downLoad(HttpServletResponse response, @RequestParam(value = "id") Long id) {
        BizFile byId = bizFileService.getById(id);
        if (byId == null) {
            throw BizException.wrap("未找到该文件!");
        }
        // path是指欲下载的文件的路径
        File file = new File(byId.getFilePath());
        // 取得文件名
        String filename = file.getName();
        // 以流的形式下载文件
        InputStream fis = null;
        OutputStream toClient = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(byId.getFilePath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(new String(filename.getBytes())));
            toClient = response.getOutputStream();
//            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setContentType("application/x-msdownload");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "No-cache");
            response.setDateHeader("Expires", 0);
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            System.out.println("下载成功");
        } catch (IOException e) {
            throw BizException.wrap("下载失败:" + e.getMessage());
        } finally {
            try {
                if (toClient != null) {
                    toClient.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}