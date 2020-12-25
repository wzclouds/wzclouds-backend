package com.github.wzclouds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.web.bind.annotation.RestController;

/**
 * Eureka 服务端
 * https://www.cnblogs.com/chry/p/7992885.html
 *
 * @author tyh
 * @createTime 2017-11-24 13:43
 */
@SpringBootApplication
@EnableEurekaServer
@RestController
public class EurekaApplication {


    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }

}
