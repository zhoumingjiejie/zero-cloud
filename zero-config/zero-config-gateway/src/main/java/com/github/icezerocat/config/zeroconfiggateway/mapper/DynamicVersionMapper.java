package com.github.icezerocat.config.zeroconfiggateway.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.icezerocat.config.zeroconfiggateway.entity.DynamicVersion;

/**
 * Description: 路由版本Mapper
 * CreateDate:  2020/11/13 13:27
 *
 * @author zero
 * @version 1.0
 */
public interface DynamicVersionMapper extends BaseMapper<DynamicVersion> {

    /**
     * 获取最后一次发布的版本号
     *
     * @return 版本号
     */
    Long getLastVersion();
}
