package com.github.wzclouds.gateway.fallback;


import com.github.wzclouds.base.R;
import com.github.wzclouds.exception.code.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 响应超时熔断处理器
 *
 * @author wzclouds
 */
@RestController
@Slf4j
public class FallbackController {

    @RequestMapping("/fallback")
    public Mono<R> fallback() {
        log.info("fallback");
        return Mono.just(R.fail(ExceptionCode.SYSTEM_TIMEOUT));
    }
}
