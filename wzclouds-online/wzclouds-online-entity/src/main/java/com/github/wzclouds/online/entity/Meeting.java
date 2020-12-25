package com.github.wzclouds.online.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.wzclouds.base.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 实体类
 * 
 * </p>
 *
 * @author wz
 * @since 2020-11-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b_meeting")
@ApiModel(value = "Meeting", description = "")
public class Meeting extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 会议名称
     */
    @ApiModelProperty(value = "会议名称")
    @NotEmpty(message = "会议名称不能为空")
    @Length(max = 32, message = "会议名称长度不能超过32")
    @TableField("name")
    private String name;

    /**
     * 会议描述
     */
    @ApiModelProperty(value = "会议描述")
    @Length(max = 400, message = "会议描述长度不能超过400")
    @TableField("descs")
    private String descs;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @TableField("end_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endTime;

    /**
     * 房主
     */
    @ApiModelProperty(value = "房主")
    @NotNull(message = "房主不能为空")
    @TableField("owner")
    private Long owner;

    /**
     * 会议标签
     */
    @ApiModelProperty(value = "会议标签")
    @Length(max = 128, message = "会议标签长度不能超过128")
    @TableField("tags")
    private String tags;

    /**
     * 是否加密
     */
    @ApiModelProperty(value = "是否加密")
    @TableField("ispassword")
    private Boolean ispassword;

    /**
     * 会议密码
     */
    @ApiModelProperty(value = "会议密码")
    @Length(max = 255, message = "会议密码长度不能超过255")
    @TableField("password")
    private String password;

    /**
     * 会议最大用户数
     */
    @ApiModelProperty(value = "会议最大用户数")
    @TableField("max_client")
    private Integer maxClient;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;
    @Builder
    public Meeting(Long id, Long createUser, LocalDateTime createTime, Long updateUser, LocalDateTime updateTime, 
                    String name, String descs, LocalDateTime endTime, Long owner, String tags,
                   Boolean ispassword, String password, Integer maxClient) {
        this.id = id;
        this.createUser = createUser;
        this.createTime = createTime;
        this.updateUser = updateUser;
        this.updateTime = updateTime;
        this.name = name;
        this.descs = descs;
        this.endTime = endTime;
        this.owner = owner;
        this.tags = tags;
        this.ispassword = ispassword;
        this.password = password;
        this.maxClient = maxClient;
    }

}
