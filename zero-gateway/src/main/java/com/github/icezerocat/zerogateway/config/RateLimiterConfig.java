package com.github.icezerocat.zerogateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * Description: 路由限流配置
 * CreateDate:  2020/11/11 15:22
 *
 * @author zero
 * @version 1.0
 */
@Configuration
public class RateLimiterConfig {
    @Bean(value = "remoteAddrKeyResolver")
    public KeyResolver remoteAddrKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}
