package com.github.icezerocat.zero.dynamic.authorization.domain.user;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户职权到资源地址的映射对象
 *
 * @author LiKe
 * @version 1.0.0
 * @date 2020-08-03 13:10
 */
@Data
@NoArgsConstructor
public class UserAuthorityResourceAddressMapping {

    /**
     * 缓存前缀
     */
    public static final String CACHE_PREFIX = "user-authority";

    /**
     * 用户职权名
     */
    private String userAuthorityName;

    /**
     * 资源地址
     */
    private String resourceAddress;

}
