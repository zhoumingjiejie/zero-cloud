package com.github.icezerocat.zero.dynamic.authorization.domain.client;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户端访问范围到资源路径的映射对象
 *
 * @author LiKe
 * @version 1.0.0
 * @date 2020-08-03 14:00
 */
@Data
@NoArgsConstructor
public class ClientAccessScopeResourceAddressMapping {

    public static final String CACHE_SUFFIX = "client-access-scope";

    /**
     * 客户端访问范围名称
     */
    private String clientAccessScopeName;

    /**
     * 资源路径
     */
    private String resourceAddress;

}
