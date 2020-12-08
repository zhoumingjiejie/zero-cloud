package com.github.icezerocat.zeroopenfeign.gateway.service;

import com.github.icezerocat.zerocommon.http.HttpResult;
import com.github.icezerocat.zeroopenfeign.constant.FeignClientName;
import com.github.icezerocat.zeroopenfeign.gateway.model.GatewayRouteDefinition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Description: 网关路由服务
 * CreateDate:  2020/11/15 12:05
 *
 * @author zero
 * @version 1.0
 */
@FeignClient(FeignClientName.ZERO_GATEWAY)
public interface GatewayRouteFeignService {

    /**
     * 更新路由
     *
     * @param gatewayRouteDefinitionList 路由模型列表
     * @return 更新结果
     */
    @PostMapping("/route/updateList")
    HttpResult<String> updateList(@RequestBody List<GatewayRouteDefinition> gatewayRouteDefinitionList);

    @PostMapping("/route/say")
    HttpResult<String> say();

}
