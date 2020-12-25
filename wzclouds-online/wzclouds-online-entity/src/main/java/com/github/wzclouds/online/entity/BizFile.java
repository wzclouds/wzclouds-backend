package com.github.wzclouds.online.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.wzclouds.base.entity.Entity;
import com.github.wzclouds.constant.FileUploadType;
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
 * <p>
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
@TableName("c_biz_file")
@ApiModel(value = "BizFile", description = "")
public class BizFile extends Entity<String> {

    private static final long serialVersionUID = 1L;

    /**
     * 文件名
     */
    @ApiModelProperty(value = "文件名")
    @Length(max = 128, message = "文件名长度不能超过128")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "")
    @Length(max = 128, message = "长度不能超过128")
    @TableField("file_path")
    private String filePath;

    @ApiModelProperty(value = "")
    @Length(max = 255, message = "长度不能超过255")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "文件类型")
    @Length(max = 32, message = "长度不能超过32")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "业务id")
    @Length(max = 21, message = "长度不能超过21")
    @TableField("biz_id")
    private String bizId;


    @ApiModelProperty(value = "业务类型")
    @Length(max = 16, message = "长度不能超过16")
    @TableField("biz_type")
    private FileUploadType bizType;

    @ApiModelProperty(value = "会议id")
    @Length(max = 21, message = "长度不能超过21")
    @TableField("meeting_id")
    private String meetingId;

    @Builder
    public BizFile(String id, LocalDateTime createTime, LocalDateTime updateTime, String createUser, String updateUser,
                   String name, String filePath, String url, String type, String bizId, FileUploadType bizType, String meetingId) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.name = name;
        this.filePath = filePath;
        this.url = url;
        this.type = type;
        this.bizId = bizId;
        this.bizType = bizType;
        this.meetingId = meetingId;
    }
}
