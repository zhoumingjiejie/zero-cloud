package com.github.icezerocat.config.zeroconfiggateway.service;


import com.github.icezerocat.zeroopenfeign.gateway.model.GatewayRouteDefinition;

import java.util.List;

/**
 * Description: 路由数据库service
 * CreateDate:  2020/11/13 17:02
 *
 * @author zero
 * @version 1.0
 */
public interface RoutesService {


    /**
     * 返回组装后网关需要的路由信息
     *
     * @return 路由信息模型
     */
    List<GatewayRouteDefinition> getRouteDefinitions();
}
