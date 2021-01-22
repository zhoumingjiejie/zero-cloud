package com.github.icezerocat.resource.accesscontrol;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description: 自定义访问决策管理器
 * CreateDate:  2021/1/13 18:32
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
    private String resourceId;

    /**
     * Description: 实现该资源服务的访问控制<br>
     * <dl>
     *     <dt>Access control principles:</dt>
     *     <dd>{@link org.springframework.security.oauth2.provider.OAuth2Authentication#isClientOnly()}{@code = true}: 验证客户端权限</dd>
     *     <dd>
     *         {@link org.springframework.security.oauth2.provider.OAuth2Authentication#isClientOnly()}{@code = false}: <br>
     *             - 如果是第一方客户端前端 ({@code CLIENT_AUTHORITY_FIRST_PARTY_FRONTEND_CLIENT}), 校验用户权限;<br>
     *             - 如果是第三方应用, 校验用户全和客户端的权限;
     *     </dd>
     * </dl>
     *
     * @param authentication   认证方式  {@link org.springframework.security.oauth2.provider.OAuth2Authentication}
     * @param object           过滤器调用对象 {@link org.springframework.security.web.FilterInvocation}
     * @param configAttributes 资源权限标识：由 {@link CustomFilterInvocationSecurityMetadataSource}
     *                         组织的资源标识:
     *                         <pre>
     *                                                                                                   - ClientAccessScope: ClientAccessScope.CACHE_PREFIX@ClientAccessScopeName<br>
     *                                                                                                   - ClientAuthority: ClientAuthority.CACHE_PREFIX@ClientAuthorityName<br>
     *                                                                                                   - UserAuthority: UserAuthority.CACHE_PREFIX@UserAuthorityName
     *                                                                         </pre>
     * @see AccessDecisionManager#decide(Authentication, Object, Collection)
     * @see CustomFilterInvocationSecurityMetadataSource#getAttributes(Object)
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        final OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        final FilterInvocation filterInvocation = (FilterInvocation) object;
        final String resourceAddress =
                StringUtils.join(filterInvocation.getRequestUrl(), CustomFilterInvocationSecurityMetadataSource.AT, Objects.requireNonNull(resourceId, "资源服务器 ID 未定义!"));

        final boolean clientOnly = oAuth2Authentication.isClientOnly();
        //主体名
        final String principalName = oAuth2Authentication.getName();
        //元数据源
        final Set<String> metadataSource = configAttributes.stream().map(ConfigAttribute::getAttribute).collect(Collectors.toSet());

        if (clientOnly) {
            log.debug("Access controller :: 请求来自第一方客户端 ...");
            final Set<String> clientAuthorities = oAuth2Authentication.getOAuth2Request().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            if (metadataSource.stream()
                    .filter(configAttrStr -> StringUtils.startsWith(configAttrStr, CustomFilterInvocationSecurityMetadataSource.CONFIG_ATTR_PREFIX_CLIENT_AUTHORITY))
                    .noneMatch(filteredConfigAttrStr ->
                            org.apache.commons.collections4.CollectionUtils.containsAny(clientAuthorities,
                                    StringUtils.substring(filteredConfigAttrStr,
                                            CustomFilterInvocationSecurityMetadataSource.CONFIG_ATTR_PREFIX_CLIENT_AUTHORITY.length())))
            ) {
                throw new InsufficientAuthenticationException(String.format("Access controller :: denied :: (客户端: %s) 没有足够的权限访问该资源: %s", principalName, resourceAddress));
            }
        } else {
            log.debug("Access controller :: 请求可能来自第一方前端 ...");
            final Set<String> userAuthorities = oAuth2Authentication.getUserAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

            // ~ 校验用户权限
            if (metadataSource.stream()
                    .filter(configAttrStr -> StringUtils.startsWith(configAttrStr, CustomFilterInvocationSecurityMetadataSource.CONFIG_ATTR_PREFIX_USER_AUTHORITY))
                    .noneMatch(filteredConfigAttrStr ->
                            org.apache.commons.collections4.CollectionUtils.containsAny(userAuthorities,
                                    StringUtils.substring(filteredConfigAttrStr,
                                            CustomFilterInvocationSecurityMetadataSource.CONFIG_ATTR_PREFIX_USER_AUTHORITY.length())))
            ) {
                throw new InsufficientAuthenticationException(String.format("Access controller :: denied :: (用户: %s) 没有足够的权限访问该资源: %s", principalName, resourceAddress));
            }

            if (!org.apache.commons.collections4.CollectionUtils.containsAny(userAuthorities, CLIENT_AUTHORITY_FIRST_PARTY_FRONTEND_CLIENT)) {
                log.debug("Access controller :: 请求来自第三方客户端 ...");
                final Set<String> clientScopeNames = oAuth2Authentication.getOAuth2Request().getScope();
                // ~ 第三方应用: 还需要客户端 SCOPE
                if (metadataSource.stream()
                        .filter(configAttrStr ->
                                StringUtils.startsWith(configAttrStr, CustomFilterInvocationSecurityMetadataSource.CONFIG_ATTR_PREFIX_CLIENT_ACCESS_SCOPE))
                        .noneMatch(filteredConfigAttrStr ->
                                CollectionUtils.containsAny(clientScopeNames,
                                        StringUtils.substring(filteredConfigAttrStr,
                                                CustomFilterInvocationSecurityMetadataSource.CONFIG_ATTR_PREFIX_CLIENT_ACCESS_SCOPE.length())))
                ) {
                    throw new InsufficientAuthenticationException(String.format("Access controller :: denied :: (客户端: %s) 的方位范围不包括资源: %s", principalName, resourceAddress));
                }
            }
        }
    }

    /**
     * 支持配置属性
     * @param attribute 属性
     * @return true
     */
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

    /**
     * 设置资源ID
     *
     * @param resourceId 资源ID
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
