package com.github.icezerocat.zero.dynamic.resource.config.support.accesscontrol;

import com.github.icezerocat.zerocommon.constant.Oauth2RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description: 用于提供最终的访问决策控制.
 * CreateDate:  2021/8/10 20:40
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

    /**
     * 第一方客户端前端 CLIENT_AUTHORITY 名称
     */
    private static final String CLIENT_AUTHORITY_FIRST_PARTY_FRONTEND_CLIENT = "FIRST_PARTY_FRONTEND_CLIENT";

    /**
     * 资源服务 ID
     */
    private String resourceId = "resource-server";

    /**
     * 设置资源ID
     *
     * @param resourceId 资源ID
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        log.warn("自定义访问决策管理器:{}\n{}\n{}", authentication, object, configAttributes);
        log.warn("自定义访问决策管理器:{}", authentication.getAuthorities());
        final OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        final FilterInvocation filterInvocation = (FilterInvocation) object;
        final String resourceAddress =
                StringUtils.join(filterInvocation.getRequestUrl(), Oauth2RedisKey.AT.getKey(), Objects.requireNonNull(resourceId, "资源服务器 ID 未定义!"));

        final boolean clientOnly = oAuth2Authentication.isClientOnly();
        //主体名
        final String principalName = oAuth2Authentication.getName();
        log.debug("主题名：{}", principalName);
        //元数据源
        final Set<String> metadataSource = configAttributes.stream().map(ConfigAttribute::getAttribute).collect(Collectors.toSet());

        if (clientOnly) {
            //内部客户端模式走这里，使用client-authority客户端职权进行鉴权
            log.debug("Access controller :: 请求来自第一方【客户端】 ...");
            final Set<String> clientAuthorities = oAuth2Authentication.getOAuth2Request().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            if (metadataSource.stream()
                    //过滤获取 client-authority@ 开头的数据
                    .filter(configAttrStr -> StringUtils.startsWith(configAttrStr, Oauth2RedisKey.CONFIG_ATTR_PREFIX_CLIENT_AUTHORITY.getKey()))
                    .noneMatch(filteredConfigAttrStr -> clientAuthorities.contains(
                            StringUtils.substring(filteredConfigAttrStr, Oauth2RedisKey.CONFIG_ATTR_PREFIX_CLIENT_AUTHORITY.getKey().length())
                    ))
            ) {
                throw new InsufficientAuthenticationException(String.format("Access controller :: denied :: (客户端: %s) 没有足够的权限访问该资源: %s", principalName, resourceAddress));
            }
        } else {
            //内部隐式、密码、授权模式走这里
            log.debug("Access controller :: 请求可能来自第一方【前端】 ...");
            final Set<String> userAuthorities = oAuth2Authentication.getUserAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

            // ~ 校验用户权限：使用user-authority用户端职权进行鉴权
            if (metadataSource.stream()
                    .filter(configAttrStr -> StringUtils.startsWith(configAttrStr, Oauth2RedisKey.CONFIG_ATTR_PREFIX_USER_AUTHORITY.getKey()))
                    .noneMatch(filteredConfigAttrStr -> userAuthorities.contains(
                            StringUtils.substring(filteredConfigAttrStr, Oauth2RedisKey.CONFIG_ATTR_PREFIX_USER_AUTHORITY.getKey().length()))
                    )
            ) {
                throw new InsufficientAuthenticationException(String.format("Access controller :: denied :: (用户: %s) 没有足够的权限访问该资源: %s", principalName, resourceAddress));
            }

            //使用client-access-scope客户端访问范围进行鉴权
            if (!org.apache.commons.collections4.CollectionUtils.containsAny(userAuthorities, Collections.singleton(CLIENT_AUTHORITY_FIRST_PARTY_FRONTEND_CLIENT))) {
                log.debug("Access controller :: 请求来自第三方【客户端】 ...");
                final Set<String> clientScopeNames = oAuth2Authentication.getOAuth2Request().getScope();
                // ~ 第三方应用: 还需要客户端 SCOPE
                if (metadataSource.stream()
                        .filter(configAttrStr ->
                                StringUtils.startsWith(configAttrStr, Oauth2RedisKey.CONFIG_ATTR_PREFIX_CLIENT_ACCESS_SCOPE.getKey()))
                        .noneMatch(filteredConfigAttrStr -> clientScopeNames.contains(
                                StringUtils.substring(filteredConfigAttrStr, Oauth2RedisKey.CONFIG_ATTR_PREFIX_CLIENT_ACCESS_SCOPE.getKey().length()))
                        )
                ) {
                    throw new InsufficientAuthenticationException(String.format("Access controller :: denied :: (客户端: %s) 的方位范围不包括资源: %s", principalName, resourceAddress));
                }
            }
        }
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        log.debug("CustomFilterInvocationSecurityMetadataSource :: supports :: {}", clazz.getCanonicalName());
        // ~ FilterInvocation: 持有与 HTTP 过滤器相关的对象
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
