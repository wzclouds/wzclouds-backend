package com.github.wzclouds.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zlt
 */
@Data
@ConfigurationProperties(prefix = "wzclouds.file")
@Component
public class FileServerProperties {
    /**
     * 文件访问前缀
     */
    private String uriPrefix = "http://127.0.0.1:10086/";
    /**
     * 文件存储路径
     */
    private String storagePath = "/data/projects/uploadfile/";
}
