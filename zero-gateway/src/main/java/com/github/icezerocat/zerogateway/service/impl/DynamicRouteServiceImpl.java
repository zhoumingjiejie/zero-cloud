package com.github.icezerocat.zerogateway.service.impl;


import com.github.icezerocat.zerogateway.dto.GatewayFilterDefinition;
import com.github.icezerocat.zerogateway.dto.GatewayPredicateDefinition;
import com.github.icezerocat.zerogateway.dto.GatewayRouteDefinition;
import com.github.icezerocat.zerogateway.service.DynamicRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 动态路由服务
 * CreateDate:  2020/11/11 12:45
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@SuppressWarnings("unused")
@Service("dynamicRouteService")
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware, DynamicRouteService {

    @Value("{gateway.route.urlStartsWith:http}")
    private String startsWith;

    private final RouteDefinitionWriter routeDefinitionWriter;
    private ApplicationEventPublisher publisher;

    public DynamicRouteServiceImpl(RouteDefinitionWriter routeDefinitionWriter) {
        this.routeDefinitionWriter = routeDefinitionWriter;
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 增加路由
     *
     * @param definition 路由
     * @return 结果信息
     */
    @Override
    public String add(RouteDefinition definition) {
        this.routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }

    /**
     * 更新路由
     *
     * @param definition 路由
     * @return 结果信息
     */
    @Override
    public String update(RouteDefinition definition) {
        try {
            this.delete(definition.getId());
        } catch (Exception e) {
            return "update fail, not find route  routeId: " + definition.getId().concat("\t").concat(e.getMessage());
        }
        try {
            this.routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "success";
        } catch (Exception e) {
            return "update route fail : ".concat(e.getMessage());
        }
    }

    @Override
    public String update(List<GatewayRouteDefinition> gatewayRouteDefinitionList) {
        String result = "success";
        StringBuilder stringBuilder = new StringBuilder();
        for (GatewayRouteDefinition gatewayRouteDefinition : gatewayRouteDefinitionList) {
            RouteDefinition routeDefinition = this.assembleRouteDefinition(gatewayRouteDefinition);
            String update = this.update(routeDefinition);
            if (!"success".equals(update)) {
                result = "fail:";
                stringBuilder.append(gatewayRouteDefinition.getId()).append(",");
            }
        }
        if (stringBuilder.length() > 0) {
            result = result.concat(stringBuilder.substring(0, stringBuilder.length() - 1));
        }
        return result;
    }

    /**
     * 删除路由
     *
     * @param id 路由id
     * @return Mono
     */
    @Override
    public Mono<ResponseEntity<Object>> delete(String id) {
        return this.routeDefinitionWriter.delete(Mono.just(id))
                .then(Mono.defer(() -> Mono.just(ResponseEntity.ok().build())))
                .onErrorResume((t) -> t instanceof NotFoundException, (t) -> Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * 转换成路由对象
     *
     * @param gatewayRouteDefinition 路由模型
     * @return 路由
     */
    @Override
    public RouteDefinition assembleRouteDefinition(GatewayRouteDefinition gatewayRouteDefinition) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(gatewayRouteDefinition.getId());
        definition.setOrder(gatewayRouteDefinition.getOrder());

        //设置断言
        List<PredicateDefinition> pdList = new ArrayList<>();
        List<GatewayPredicateDefinition> gatewayPredicateDefinitionList = gatewayRouteDefinition.getPredicates();
        if (gatewayPredicateDefinitionList != null) {
            for (GatewayPredicateDefinition gpDefinition : gatewayPredicateDefinitionList) {
                PredicateDefinition predicate = new PredicateDefinition();
                predicate.setArgs(gpDefinition.getArgs());
                predicate.setName(gpDefinition.getName());
                pdList.add(predicate);
            }
        }
        definition.setPredicates(pdList);

        //设置过滤器
        List<FilterDefinition> filters = new ArrayList<>();
        List<GatewayFilterDefinition> gatewayFilters = gatewayRouteDefinition.getFilters();
        if (gatewayFilters != null) {
            for (GatewayFilterDefinition filterDefinition : gatewayFilters) {
                FilterDefinition filter = new FilterDefinition();
                filter.setName(filterDefinition.getName());
                filter.setArgs(filterDefinition.getArgs());
                filters.add(filter);
            }
        }
        definition.setFilters(filters);

        URI uri;
        if (gatewayRouteDefinition.getUri().startsWith(this.startsWith)) {
            uri = UriComponentsBuilder.fromHttpUrl(gatewayRouteDefinition.getUri()).build().toUri();
        } else {
            uri = URI.create(gatewayRouteDefinition.getUri());
        }
        definition.setUri(uri);
        log.debug("route:{}", definition);
        return definition;
    }
}
