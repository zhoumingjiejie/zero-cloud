package com.github.icezerocat.zerogateway.service;

import com.github.icezerocat.zerogateway.dto.GatewayRouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Description: 动态路由服务
 * CreateDate:  2020/11/13 16:45
 *
 * @author zero
 * @version 1.0
 */
public interface DynamicRouteService {
    /**
     * 增加路由
     *
     * @param definition 路由
     * @return 结果信息
     */
    String add(RouteDefinition definition);

    /**
     * 更新路由
     *
     * @param definition 路由
     * @return 结果信息
     */
    String update(RouteDefinition definition);

    /**
     * 更新路由
     *
     * @param gatewayRouteDefinitionList 路由模型
     * @return 结果信息
     */
    String update(List<GatewayRouteDefinition> gatewayRouteDefinitionList);

    /**
     * 删除路由
     *
     * @param id 路由id
     * @return Mono
     */
    Mono<ResponseEntity<Object>> delete(String id);

    /**
     * 转换成路由对象
     *
     * @param gatewayRouteDefinition 路由模型
     * @return 路由
     */
    RouteDefinition assembleRouteDefinition(GatewayRouteDefinition gatewayRouteDefinition);
}
