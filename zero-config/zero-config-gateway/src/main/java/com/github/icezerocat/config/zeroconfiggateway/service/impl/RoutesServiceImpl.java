package com.github.icezerocat.config.zeroconfiggateway.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.icezerocat.config.zeroconfiggateway.entity.GatewayRoutes;
import com.github.icezerocat.config.zeroconfiggateway.mapper.GatewayRoutesMapper;
import com.github.icezerocat.config.zeroconfiggateway.service.RoutesService;
import com.github.icezerocat.zeroopenfeign.gateway.model.GatewayFilterDefinition;
import com.github.icezerocat.zeroopenfeign.gateway.model.GatewayPredicateDefinition;
import com.github.icezerocat.zeroopenfeign.gateway.model.GatewayRouteDefinition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 路由数据库service
 * CreateDate:  2020/11/13 17:15
 *
 * @author zero
 * @version 1.0
 */
@Service("routesService")
public class RoutesServiceImpl implements RoutesService {

    @Resource
    private GatewayRoutesMapper gatewayRoutesMapper;

    @Override
    public List<GatewayRouteDefinition> getRouteDefinitions() {
        List<GatewayRouteDefinition> gatewayRouteDefinitionList = new ArrayList<>();
        QueryWrapper<GatewayRoutes> query = Wrappers.query();
        query.eq("is_ebl", 0).eq("is_del", 0);
        List<GatewayRoutes> gatewayRoutesList = this.gatewayRoutesMapper.selectList(query);
        for (GatewayRoutes gatewayRoutes : gatewayRoutesList) {
            GatewayRouteDefinition routeDefinition = new GatewayRouteDefinition();
            routeDefinition.setId(gatewayRoutes.getRouteId());
            routeDefinition.setUri(gatewayRoutes.getRouteUri());
            routeDefinition.setFilters(JSON.parseArray(gatewayRoutes.getFilters(), GatewayFilterDefinition.class));
            routeDefinition.setPredicates(JSON.parseArray(gatewayRoutes.getPredicates(), GatewayPredicateDefinition.class));
            gatewayRouteDefinitionList.add(routeDefinition);
        }
        return gatewayRouteDefinitionList;
    }
}
