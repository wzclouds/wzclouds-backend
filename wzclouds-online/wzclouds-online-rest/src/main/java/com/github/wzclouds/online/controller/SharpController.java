package com.github.wzclouds.online.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.wzclouds.base.R;
import com.github.wzclouds.context.BaseContextHandler;
import com.github.wzclouds.database.mybatis.conditions.Wraps;
import com.github.wzclouds.database.mybatis.conditions.query.LbqWrapper;
import com.github.wzclouds.online.dto.SharpRemoveDTO;
import com.github.wzclouds.online.dto.SharpTransDTO;
import com.github.wzclouds.online.entity.Sharp;
import com.github.wzclouds.online.service.SharpService;
import com.github.wzclouds.dozer.DozerUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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
@RequestMapping("/anno/sharp")
@Api(value = "Sharp", tags = "白板相关")
public class SharpController {

    @Autowired
    private SharpService sharpService;
    @Autowired
    private DozerUtils dozerUtils;

    /**
     * 保存绘制体单项
     * @param dto
     * @return
     */
    @ApiOperation(value = "保存绘制体单项", notes = "保存绘制体单项")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R<Sharp> save(@RequestBody SharpTransDTO dto) {
        List<List<Integer>> path = dto.getPath();
        if (dto.getPath() != null) {
            dto.setPathNum(path.toString());
        }
        LocalDateTime now = LocalDateTime.now();
        List<Double> matrix = dto.getMatrix();
        dto.setScaleX(matrix.get(0));
        dto.setScaleY(matrix.get(3));
        dto.setX(matrix.get(4));
        dto.setY(matrix.get(5));
        dto.setCentPointNum(dto.getCentPoint().toString());
        dto.setCreateUser(dto.getCreateUser());
        dto.setCreateTime(now);
        dto.setUpdateUser(dto.getCreateUser());
        dto.setUpdateTime(now);
        Sharp sharp = dozerUtils.map(dto, Sharp.class);
        sharpService.save(sharp);
        return R.success(sharp);
    }

    /**
     * 查找该房间的绘制体集合
     * @param id
     * @return
     */
    @ApiOperation(value = "查找该房间的绘制体集合", notes = "查找该房间的绘制体集合")
    @RequestMapping(value = "/findSharpByMeetingId", method = RequestMethod.GET)
    public R<List<SharpTransDTO>> findSharpByMeetingId(@RequestParam(value = "id") Long id) {
        LbqWrapper<Sharp> wrapper = Wraps.lbQ();
        wrapper.eq(Sharp::getMeetingId, id).orderByAsc(Sharp::getCreateTime);
        List<SharpTransDTO> list = dozerUtils.mapList(sharpService.list(wrapper), SharpTransDTO.class);
        list.forEach(dto -> {
            List<List<Integer>> path = (List<List<Integer>>) JSONObject.parse(dto.getPathNum());
            dto.setPath(path);
            Double[] matrix = {dto.getScaleX(), 0D, 0D, dto.getScaleY(), dto.getX(), dto.getY()};
            List<Double> centPoint = (List<Double>) JSONObject.parse(dto.getCentPointNum());
            dto.setMatrix(Arrays.asList(matrix));
            dto.setCentPoint(centPoint);
        });
        return R.success(list);
    }

    /**
     * 更新绘制体单项
     * @param dto
     * @return
     */
    @ApiOperation(value = "更新绘制体单项", notes = "更新绘制体单项")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R<Sharp> update(@RequestBody SharpTransDTO dto) {
        String userId = null;
        if (BaseContextHandler.getUserId() != null) {
            userId = BaseContextHandler.getUserIdStr();
        }
        List<Double> matrix = dto.getMatrix();
        Sharp sharp = Sharp.builder()
                .id(dto.getId())
                .scaleX(matrix.get(0))
                .scaleY(matrix.get(3))
                .x(matrix.get(4))
                .y(matrix.get(5))
                .updateUser(userId)
                .updateTime(LocalDateTime.now())
                .build();
        sharpService.updateById(sharp);
        return R.success(sharp);
    }

    /**
     * 移除绘制体
     * @param dto
     * @return
     */
    @ApiOperation(value = "移除绘制体", notes = "移除绘制体")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public R<Boolean> remove(@RequestBody SharpRemoveDTO dto) {
        if (CollUtil.isEmpty(dto.getIds())) {
            return R.fail("无清除数据");
        }
        return R.success(sharpService.removeByIds(dto.getIds()));
    }
}
