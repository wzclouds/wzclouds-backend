package com.github.wzclouds.online.dto;

import com.github.wzclouds.online.entity.BizFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString(callSuper = true)
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BizFileReqDTO", description = "文件查询入参")
public class BizFileResDTO extends BizFile {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名, 对应业务id")
    private String userName;
}
