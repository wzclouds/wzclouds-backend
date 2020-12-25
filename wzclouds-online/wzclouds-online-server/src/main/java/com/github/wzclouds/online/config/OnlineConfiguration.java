package com.github.wzclouds.online.config;

import com.github.wzclouds.boot.config.BaseConfig;
import com.github.wzclouds.common.UserSession;
import com.github.wzclouds.online.controller.OnlineSocket;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tyh
 * @createTime 2017-12-15 14:42
 */
@Configuration
@AutoConfigureAfter({OnlineSocket.class})
public class OnlineConfiguration extends BaseConfig implements WebSocketConfigurer {

    @Bean
    ConcurrentHashMap<String, UserSession> getUserSessions() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public OnlineSocket onlineSocket() {
        return new OnlineSocket();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(onlineSocket(), "/wss")
                .setAllowedOrigins("*");
    }

//    @Bean
//    public MyEndpointConfigure newConfigure()
//    {
//        return new MyEndpointConfigure();
//    }
}
