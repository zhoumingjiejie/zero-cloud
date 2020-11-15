package com.github.icezerocat.config.zeroconfiggateway.service;

import com.github.icezerocat.config.zeroconfiggateway.entity.DynamicVersion;

/**
 * Description: 路由器版本
 * CreateDate:  2020/11/14 0:17
 *
 * @author zero
 * @version 1.0
 */
public interface DynamicVersionService {

    /**
     * 添加路由器版本
     *
     * @param version 版本数据
     * @return 添加结果
     */
    int add(DynamicVersion version);

    /**
     * 获取最后一次发布的版本号
     *
     * @return 版本号
     */
    Long getLastVersion();
}
