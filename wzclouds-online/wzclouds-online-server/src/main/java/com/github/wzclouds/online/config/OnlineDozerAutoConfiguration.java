package com.github.wzclouds.online.config;


import com.github.dozermapper.core.Mapper;
import com.github.wzclouds.dozer.DozerUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Dozer spring auto configuration.
 *
 * @author tangyh
 * @date 2020-04-30
 */
@Configuration
public class OnlineDozerAutoConfiguration {
    @Bean
    public DozerUtils getDozerUtils(Mapper mapper) {
        return new DozerUtils(mapper);
    }
}
