package com.github.icezerocat.zerogateway.web.controller;

import com.github.icezerocat.zerocommon.http.HttpResult;
import com.github.icezerocat.zerogateway.dto.GatewayRouteDefinition;
import com.github.icezerocat.zerogateway.service.DynamicRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Description: 路由控制器
 * CreateDate:  2020/11/11 13:30
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("route")
public class RouteController {

    private final DynamicRouteService dynamicRouteService;

    public RouteController(DynamicRouteService dynamicRouteService) {
        this.dynamicRouteService = dynamicRouteService;
    }

    /**
     * 增加路由
     *
     * @param gatewayRouteDefinition 路由模型
     * @return 返回结果
     */
    @PostMapping("/add")
    public String add(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        String flag = "fail";
        try {
            RouteDefinition definition = this.dynamicRouteService.assembleRouteDefinition(gatewayRouteDefinition);
            flag = this.dynamicRouteService.add(definition);
        } catch (Exception e) {
            log.error("add route exception : {}", e.getMessage());
            flag = flag.concat(":").concat(e.getMessage());
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除路由
     *
     * @param id 路由ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable String id) {
        try {
            return this.dynamicRouteService.delete(id);
        } catch (Exception e) {
            log.error("delete route fail : {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新路由
     *
     * @param gatewayRouteDefinition 路由模型
     * @return 更新结果
     */
    @PostMapping("/update")
    public String update(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        RouteDefinition definition = this.dynamicRouteService.assembleRouteDefinition(gatewayRouteDefinition);
        return this.dynamicRouteService.update(definition);
    }

    /**
     * 更新路由
     *
     * @param gatewayRouteDefinitionList 路由模型列表
     * @return 更新结果
     */
    @PostMapping("/updateList")
    public HttpResult<String> updateList(@RequestBody List<GatewayRouteDefinition> gatewayRouteDefinitionList) {
        return HttpResult.ok(this.dynamicRouteService.update(gatewayRouteDefinitionList));
    }

    @PostMapping("say")
    public HttpResult<String> say() {
        return HttpResult.ok();
    }
}
