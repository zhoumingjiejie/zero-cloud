package com.github.icezerocat.zerogateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description: 全局过滤器
 * CreateDate:  2020/11/11 15:40
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Component
public class RequestGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getURI().toString();
        log.debug("全局过滤器 uri : {}", uri);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
