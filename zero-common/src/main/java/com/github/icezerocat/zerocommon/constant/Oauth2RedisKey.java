package com.github.icezerocat.zerocommon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Description: oauth2 redis 缓存键
 * CreateDate:  2021/8/12 22:23
 *
 * @author zero
 * @version 1.0
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public enum Oauth2RedisKey {
    /**
     * redis键
     */
    METADATA_RESOURCE_ADDRESS_CACHE_PREFIX("metadata:resource-address", "元数据资源地址缓存前缀"),
    CLIENT_ACCESS_SCOPE("client-access-scope", "客户端访问范围"),
    CLIENT_AUTHORITY("client-authority", "客户端职权"),
    USER_AUTHORITY("user-authority", "用户端职权"),
    AT("@", "分隔符"),
    CONFIG_ATTR_PREFIX_CLIENT_ACCESS_SCOPE(CLIENT_ACCESS_SCOPE.getKey() + AT.getKey(), "前缀: 客户端访问范围"),
    CONFIG_ATTR_PREFIX_CLIENT_AUTHORITY(CLIENT_AUTHORITY.getKey() + AT.getKey(), "前缀: 客户端职权"),
    CONFIG_ATTR_PREFIX_USER_AUTHORITY(USER_AUTHORITY.getKey() + AT.getKey(), "前缀: 用户端职权"),
    ;
    /**
     * redis key
     */
    private String key;
    /**
     * 注释
     */
    private String remark;
}
