package com.github.wzclouds.online.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.wzclouds.base.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * <p>
 * 实体类
 * 
 * </p>
 *
 * @author wz
 * @since 2020-11-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b_sharp")
@ApiModel(value = "Sharp", description = "")
public class Sharp extends Entity<String> {

    private static final long serialVersionUID = 1L;

    /**
     * 类型:{PATH, IMAGE}
     */
    @ApiModelProperty(value = "类型:{PATH, IMAGE}")
    @Length(max = 255, message = "类型:{PATH, IMAGE}长度不能超过255")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "")
    @Length(max = 65535, message = "长度不能超过65535")
    @TableField("path_num")
    private String pathNum;

    @ApiModelProperty(value = "")
    @Length(max = 8, message = "长度不能超过8")
    @TableField("color")
    private String color;

    @ApiModelProperty(value = "")
    @TableField("thinkness")
    private Integer thinkness;

    /**
     * 坐标偏移x
     */
    @ApiModelProperty(value = "坐标偏移x")
    @TableField("x")
    private Double x;

    /**
     * 坐标偏移y
     */
    @ApiModelProperty(value = "坐标偏移y")
    @TableField("y")
    private Double y;

    /**
     * 缩放y
     */
    @ApiModelProperty(value = "缩放y")
    @TableField("scale_x")
    private Double scaleX;

    /**
     * 缩放y
     */
    @ApiModelProperty(value = "缩放y")
    @TableField("scale_y")
    private Double scaleY;

    /**
     * 仅限文件url
     */
    @ApiModelProperty(value = "仅限文件url")
    @Length(max = 255, message = "仅限文件url长度不能超过255")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "会议房间id")
    @TableField("meeting_id")
    private Long meetingId;

    /**
     * 原初中心点
     */
    @ApiModelProperty(value = "原初中心点")
    @Length(max = 32, message = "原初中心点长度不能超过32")
    @TableField("cent_point_num")
    private String centPointNum;


    @Builder
    public Sharp(String id, LocalDateTime createTime, LocalDateTime updateTime, String createUser, String updateUser,
                    String type, String pathNum, String color, Integer thinkness, Double x, 
                    Double y, Double scaleX, Double scaleY, String url, Long meetingId, String centPointNum) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.type = type;
        this.pathNum = pathNum;
        this.color = color;
        this.thinkness = thinkness;
        this.x = x;
        this.y = y;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.url = url;
        this.meetingId = meetingId;
        this.centPointNum = centPointNum;
    }

}
