package com.github.icezerocat.config.zeroconfiggateway.schedule;

import com.github.icezerocat.config.zeroconfiggateway.service.RoutesService;
import com.github.icezerocat.zerocommon.http.HttpResult;
import com.github.icezerocat.zerocommon.http.HttpStatus;
import com.github.icezerocat.zerocore.constants.AsyncPool;
import com.github.icezerocat.zerocore.constants.RedisKey;
import com.github.icezerocat.zerocore.utils.RedisUtil;
import com.github.icezerocat.zeroopenfeign.gateway.model.GatewayRouteDefinition;
import com.github.icezerocat.zeroopenfeign.gateway.service.GatewayRouteFeignService;
import com.google.common.primitives.Ints;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Description: 定时任务，拉取路由信息维护更新
 * <p>
 * CreateDate:  2020/11/14 16:37
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class DynamicRouteScheduling {

    private static int versionId = 0;
    private final RoutesService routesService;
    private final GatewayRouteFeignService gatewayRouteFeignService;

    /**
     * 异步-每60秒中执行一次,如果版本号不相等则获取最新路由信息并更新网关路由
     */
    @Async(AsyncPool.POOL_SCHEDULE)
    @Scheduled(cron = "*/60 * * * * ?")
    public void updateDynamicRouteInfo() {
        Set<String> routeId = new HashSet<>();
        try {
            RedisUtil redisUtil = RedisUtil.getInstance();
            //先拉取版本信息，如果版本号不想等则更新路由
            int resultVersionId = Optional.ofNullable(String.valueOf(redisUtil.get(RedisKey.GATEWAY_VERSION_KEY))).map(Ints::tryParse).orElse(-1);
            if (resultVersionId > -1 && versionId < resultVersionId) {
                List<List<GatewayRouteDefinition>> list = redisUtil.lGet(RedisKey.GATEWAY_ROUTE_KEY, 0, -1);
                List<GatewayRouteDefinition> gatewayRouteDefinitionList = new ArrayList<>();
                if (list != null) {
                    list.forEach(o -> o.forEach(t -> {
                        if (routeId.add(t.getId())) {
                            gatewayRouteDefinitionList.add(t);
                        }
                    }));
                }
                this.getResult(gatewayRouteDefinitionList);
                versionId = resultVersionId;
            }
        } catch (Exception e) {
            log.error("更新路由信息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 执行实时更新操作
     *
     * @return 更新结果
     */
    public HttpResult<String> update() {
        List<GatewayRouteDefinition> list = this.routesService.getRouteDefinitions();
        return this.getResult(list);
    }

    /**
     * 获取更新结果
     *
     * @param list 更新路由器
     * @return 更新结果
     */
    private HttpResult<String> getResult(List<GatewayRouteDefinition> list) {
        HttpResult.Build<String> build = HttpResult.Build.getInstance();
        build.setHttpStatus(HttpStatus.OK);
        build.setData(String.valueOf(list));
        //调用网关服务更新路由
        if (!CollectionUtils.isEmpty(list)) {
            HttpResult<String> result = this.gatewayRouteFeignService.updateList(list);
            log.info("更新路由结果：{}", result);
            if (!"success".equalsIgnoreCase(result.getMessage())) {
                build.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR).setMessage(result.getData());
            }
        }
        return build.complete();
    }
}
