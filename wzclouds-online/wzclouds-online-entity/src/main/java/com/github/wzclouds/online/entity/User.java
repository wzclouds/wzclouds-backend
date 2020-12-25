package com.github.wzclouds.online.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.wzclouds.base.entity.Entity;
import com.github.wzclouds.online.enumeration.UserType;
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

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * <p>
 * 实体类
 * 用户表
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
@TableName("c_user")
@ApiModel(value = "User", description = "用户表")
public class User extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 电话号/账号
     */
    @ApiModelProperty(value = "电话号/账号")
    @NotEmpty(message = "电话号/账号不能为空")
    @Length(max = 32, message = "电话号/账号长度不能超过32")
    @TableField("account")
    private String account;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @NotEmpty(message = "密码不能为空")
    @Length(max = 64, message = "密码长度不能超过64")
    @TableField("password")
    private String password;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    @NotEmpty(message = "姓名不能为空")
    @Length(max = 16, message = "姓名长度不能超过16")
    @TableField("name_")
    private String name;

    /**
     * 身份证
     */
    @ApiModelProperty(value = "身份证")
    @Length(max = 20, message = "身份证长度不能超过20")
    @TableField("id_card")
    private String idCard;

    /**
     * 外部用户id
     */
    @ApiModelProperty(value = "外部用户id")
    @TableField("out_user_id")
    private Long outUserId;

    /**
     * 外部应用app_id
     */
    @ApiModelProperty(value = "外部应用app_id")
    @Length(max = 32, message = "外部应用app_id长度不能超过32")
    @TableField("app_id")
    private String appId;

    /**
     * 用户类型 #UserType{TEMP:临时用户;OUTTER:外部用户}
     */
    @ApiModelProperty(value = "用户类型 #UserType{TEMP:临时用户;OUTTER:外部用户}")
    @TableField("type")
    private UserType type;

    @ApiModelProperty(value = "")
    @Length(max = 255, message = "长度不能超过255")
    @TableField("photo")
    private String photo;


    @Builder
    public User(Long id, Long createUser, LocalDateTime createTime, Long updateUser, LocalDateTime updateTime,
                String account, String password, String name, String idCard, Long outUserId, String appId,
                UserType type, String photo) {
        this.id = id;
        this.createUser = createUser;
        this.createTime = createTime;
        this.updateUser = updateUser;
        this.updateTime = updateTime;
        this.account = account;
        this.password = password;
        this.name = name;
        this.idCard = idCard;
        this.outUserId = outUserId;
        this.appId = appId;
        this.type = type;
        this.photo = photo;
    }

}
