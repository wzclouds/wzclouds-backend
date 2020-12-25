package com.github.wzclouds.online.dto;

import com.github.wzclouds.online.entity.BizFile;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FileEnResDTO", description = "图形文件返回实体")
public class FileEnResDTO extends BizFile {
    private static final long serialVersionUID = 1L;

    private Integer width;
    private Integer height;
}
