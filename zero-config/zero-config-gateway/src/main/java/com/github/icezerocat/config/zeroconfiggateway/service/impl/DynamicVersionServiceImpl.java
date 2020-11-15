package com.github.icezerocat.config.zeroconfiggateway.service.impl;

import com.github.icezerocat.config.zeroconfiggateway.entity.DynamicVersion;
import com.github.icezerocat.config.zeroconfiggateway.mapper.DynamicVersionMapper;
import com.github.icezerocat.config.zeroconfiggateway.service.DynamicVersionService;
import com.github.icezerocat.config.zeroconfiggateway.service.RoutesService;
import com.github.icezerocat.zerocore.constants.RedisKey;
import com.github.icezerocat.zerocore.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Description: 路由器版本
 * CreateDate:  2020/11/14 0:18
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Service("dynamicVersionService")
public class DynamicVersionServiceImpl implements DynamicVersionService {

    @Resource
    private DynamicVersionMapper dynamicVersionMapper;
    @Resource
    private RoutesService routesService;

    @Override
    public int add(DynamicVersion version) {
        version.setCreateTime(new Date());
        int result = this.dynamicVersionMapper.insert(version);
        version.setId(this.dynamicVersionMapper.getLastVersion());
        //缓存路由版本和详细信息
        RedisUtil redisUtil = RedisUtil.getInstance();
        redisUtil.set(RedisKey.GATEWAY_VERSION_KEY, version.getId());
        redisUtil.del(RedisKey.GATEWAY_ROUTE_KEY);
        redisUtil.lSet(RedisKey.GATEWAY_ROUTE_KEY, this.routesService.getRouteDefinitions());
        return result;
    }

    @Override
    public Long getLastVersion() {
        return this.dynamicVersionMapper.getLastVersion();
    }
}
