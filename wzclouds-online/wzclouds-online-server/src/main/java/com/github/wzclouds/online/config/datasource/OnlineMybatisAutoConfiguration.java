package com.github.wzclouds.online.config.datasource;


import com.github.wzclouds.database.datasource.BaseMybatisConfiguration;
import com.github.wzclouds.database.properties.DatabaseProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * 配置一些拦截器
 *
 * @author tangyh
 * @createTime 2017-11-18 0:34
 */
@Configuration
@Slf4j
public class OnlineMybatisAutoConfiguration extends BaseMybatisConfiguration {

    public OnlineMybatisAutoConfiguration(DatabaseProperties databaseProperties) {
        super(databaseProperties);
    }
}
