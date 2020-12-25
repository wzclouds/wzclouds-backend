package com.github.wzclouds;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author wzclouds
 * @createTime 2017-12-13 15:02
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"com.github.wzclouds"})
@ServletComponentScan(basePackages = {"com.github.wzclouds"})
@Slf4j
public class GatewayServerApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(GatewayServerApplication.class, args);
        Environment env = application.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "应用 '{}' 运行成功! 访问连接:\n\t" +
                        "Swagger文档: \t\thttp://{}:{}{}{}/doc.html\n\t" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path", ""),
                env.getProperty("spring.mvc.servlet.path", "")
        );
    }
}
