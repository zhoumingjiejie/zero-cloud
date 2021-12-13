package com.github.icezerocat.zero.dynamic.resource.config.support.accesscontrol;

import com.github.icezerocat.component.redisson.utils.RedisKey;
import com.github.icezerocat.component.redisson.utils.RedisUtil;
import com.github.icezerocat.zerocommon.constant.Oauth2RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description: 自定义过滤器调用安全性元数据源,SecurityMetadataSource 的提供的 Configuration Attributes 正是 AccessDecisionManager 的判断依据
 * CreateDate:  2021/8/10 20:38
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    /**
     * 缓存键: 客户端访问范围-资源路径元数据(metadata:resource-address:client-access-scope)
     */
    private static final RedisKey METADATA_CLIENT_ACCESS_SCOPE_RESOURCE_ADDRESS_CACHE_KEY =
            RedisKey.builder().prefix(Oauth2RedisKey.METADATA_RESOURCE_ADDRESS_CACHE_PREFIX.getKey()).suffix(Oauth2RedisKey.CLIENT_ACCESS_SCOPE.getKey()).build();

    /**
     * 缓存键: 客户端职权-资源路径元数据(metadata:resource-address:client-authority)
     */
    private static final RedisKey METADATA_CLIENT_AUTHORITY_RESOURCE_ADDRESS_CACHE_KEY =
            RedisKey.builder().prefix(Oauth2RedisKey.METADATA_RESOURCE_ADDRESS_CACHE_PREFIX.getKey()).suffix(Oauth2RedisKey.CLIENT_AUTHORITY.getKey()).build();

    /**
     * 缓存键: 用户端职权-资源路径元数据(metadata:resource-address:user-authority)
     */
    private static final RedisKey METADATA_USER_AUTHORITY_RESOURCE_ADDRESS_CACHE_KEY =
            RedisKey.builder().prefix(Oauth2RedisKey.METADATA_RESOURCE_ADDRESS_CACHE_PREFIX.getKey()).suffix(Oauth2RedisKey.USER_AUTHORITY.getKey()).build();

    /**
     * 资源服务 ID
     */
    private String resourceId = "resource-server";

    /**
     * 路径匹配器
     */
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 设置资源ID
     *
     * @param resourceId 资源ID
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * 访问适用于给定安全对象的属性
     *
     * @param object 被保护的对象
     * @return 适用于传入的安全对象的属性。如果没有适用的属性，则应返回 * 空集合
     * @throws IllegalArgumentException 如果传递的对象不是支持的类型异常
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        log.info("自定义过滤器调用安全性元数据源:{}", object);

        // ~ 由 supports 方法决定
        final FilterInvocation filterInvocation = (FilterInvocation) object;
        final String endpoint = filterInvocation.getRequestUrl();

        // 资源地址(/resource/access@resource-server)
        final String resourceAddress = StringUtils.join(endpoint, Oauth2RedisKey.AT.getKey(), Objects.requireNonNull(resourceId, "资源服务器 ID 未定义!"));

        // ~ 通过要访问的端点和当前资源服务器 ID 获取可访问当前资源的 ClientAuthority, UserAuthority 和 ClientAccessScope 集合,
        //   约定, 每一种权限按照约定的前缀放入集合, 便于 AccessDecisionManager.
        //   然后, AccessDecisionManager 根据 OAuth2Authentication 判断 authorities / scopes 是否在集合中

        Collection<ConfigAttribute> configAttributes = new HashSet<>();
        //添加客户端访问范围
        this.addClientAccessScope(configAttributes, resourceAddress);
        //添加客户端职权
        this.addClientAuthority(configAttributes, resourceAddress);
        //添加用户端职权
        this.addUserAuthority(configAttributes, resourceAddress);

        // ~ 为 AccessDecisionManager 提供包含匹配当前访问的资源端点的 ClientAuthority, UserAuthority, 以及 ClientAccessScope 的集合
        //   格式:
        //       - ClientAccessScope: ClientAccessScope.CACHE_PREFIX@ClientAccessScopeName
        //       - ClientAuthority: ClientAuthority.CACHE_PREFIX@ClientAuthorityName
        //       - UserAuthority: UserAuthority.CACHE_PREFIX@UserAuthorityName
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        log.debug("CustomFilterInvocationSecurityMetadataSource :: getAllConfigAttributes");
        throw new UnsupportedOperationException("不支持的操作!");
    }

    @Override
    public boolean supports(Class<?> clazz) {
        log.debug("CustomFilterInvocationSecurityMetadataSource :: supports :: {}", clazz.getCanonicalName());
        // ~ FilterInvocation: 持有与 HTTP 过滤器相关的对象
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    /**
     * 添加客户端访问范围
     *
     * @param configAttributes 配置属性
     * @param resourceAddress  资源地址
     */
    private void addClientAccessScope(Collection<ConfigAttribute> configAttributes, String resourceAddress) {
        // 获取缓存数据：客户端访问范围名称(metadata:resource-address:client-access-scope)
        final Map<Object, Object> clientAccessScopeResourceAddressMapping = RedisUtil.getInstance().hash().getAll(METADATA_CLIENT_ACCESS_SCOPE_RESOURCE_ADDRESS_CACHE_KEY);


        configAttributes.addAll(clientAccessScopeResourceAddressMapping.keySet()
                .stream()
                .filter(clientAccessScopeName -> {
                            // ACCESS_RESOURCE（redis里面map的key）
                            log.debug("clientAccessScopeName:{}", clientAccessScopeName);
                            // /resource/access@resource-server（redis里面map的value）
                            log.debug("MapUtils:{}", MapUtils.getString(clientAccessScopeResourceAddressMapping, clientAccessScopeName));
                            // /resource/access@resource-server
                            log.debug("resourceAddress:{}", resourceAddress);

                            return this.checkAccessPermissions(MapUtils.getString(clientAccessScopeResourceAddressMapping, clientAccessScopeName), resourceAddress);
                        }
                )
                // client-access-scope@ACCESS_RESOURCE
                .map(clientAccessScopeName -> new SecurityConfig(StringUtils.join(Oauth2RedisKey.CONFIG_ATTR_PREFIX_CLIENT_ACCESS_SCOPE.getKey(), clientAccessScopeName)))
                .collect(Collectors.toSet())
        );
    }

    /**
     * 添加客户端职权
     *
     * @param configAttributes 配置属性
     * @param resourceAddress  资源地址
     */
    private void addClientAuthority(Collection<ConfigAttribute> configAttributes, String resourceAddress) {
        final Map<Object, Object> clientAuthorityResourceAddressMapping = RedisUtil.getInstance().hash().getAll(METADATA_CLIENT_AUTHORITY_RESOURCE_ADDRESS_CACHE_KEY);
        configAttributes.addAll(clientAuthorityResourceAddressMapping.keySet()
                .stream()
                .filter(clientAuthorityName ->
                        this.checkAccessPermissions(MapUtils.getString(clientAuthorityResourceAddressMapping, clientAuthorityName), resourceAddress)
                )
                .map(clientAuthorityName -> new SecurityConfig(StringUtils.join(Oauth2RedisKey.CONFIG_ATTR_PREFIX_CLIENT_AUTHORITY.getKey(), clientAuthorityName)))
                .collect(Collectors.toSet())
        );
    }

    /**
     * 添加用户端职权
     *
     * @param configAttributes 配置属性
     * @param resourceAddress  资源地址
     */
    private void addUserAuthority(Collection<ConfigAttribute> configAttributes, String resourceAddress) {
        final Map<Object, Object> userAuthorityResourceAddressMapping = RedisUtil.getInstance().hash().getAll(METADATA_USER_AUTHORITY_RESOURCE_ADDRESS_CACHE_KEY);
        configAttributes.addAll(userAuthorityResourceAddressMapping.keySet()
                .stream()
                .filter(userAuthorityName ->
                        this.checkAccessPermissions(MapUtils.getString(userAuthorityResourceAddressMapping, userAuthorityName), resourceAddress)
                )
                .map(userAuthorityName -> new SecurityConfig(StringUtils.join(Oauth2RedisKey.CONFIG_ATTR_PREFIX_USER_AUTHORITY.getKey(), userAuthorityName)))
                .collect(Collectors.toSet())
        );
    }

    /**
     * 检查访问权限
     *
     * @param permissions     权限： /resource/access@resource-server
     * @param resourceAddress 访问地址： /resource/access@resource-server
     * @return 权限结果
     */
    private boolean checkAccessPermissions(String permissions, String resourceAddress) {
        if (StringUtils.isBlank(permissions) || StringUtils.isBlank(resourceAddress)) {
            return false;
        }
        //redis缓存权限
        String[] permissionsArr = permissions.split(Oauth2RedisKey.AT.getKey());
        String permissionUrl = permissionsArr[0];
        String permissionResource = permissionsArr[1];
        //访问路径
        String[] resourceAddressArr = resourceAddress.split(Oauth2RedisKey.AT.getKey());
        String addressUrl = resourceAddressArr[0];
        String addressResource = resourceAddressArr[1];
        return StringUtils.equals(permissionResource, addressResource) && this.antPathMatcher.match(permissionUrl, addressUrl);
    }

    /*public static void main(String[] args) {
        Map<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("a","av");
        stringStringHashMap.put("b","bv");
       Set<String> stringSet = stringStringHashMap.keySet().stream().peek(System.out::println).collect(Collectors.toSet());
        System.out.println(stringSet);
    }*/
}
