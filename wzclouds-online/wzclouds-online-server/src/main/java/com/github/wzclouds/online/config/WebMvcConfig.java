package com.github.wzclouds.online.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * MVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(getInjectUserResolver());
//    }
//
//    @Bean
//    public InjectUserResolver getInjectUserResolver() {
//        return new InjectUserResolver();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//        AuthServerContextHandlerInterceptor ir = authServerContextHandlerInterceptor();
//        registry.addInterceptor(ir).addPathPatterns("/**").excludePathPatterns("/login");
//        WebMvcConfigurer.super.addInterceptors(registry);
//    }
//
//    @Autowired
//    JwtTokenServerUtils jwt;
//
//    @Bean
//    public AuthServerContextHandlerInterceptor authServerContextHandlerInterceptor() {
//        AuthServerContextHandlerInterceptor ir = new AuthServerContextHandlerInterceptor(jwt);
//        return ir;
//    }
}
