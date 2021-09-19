package com.github.icezerocat.resource.accesscontrol;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;

/**
 * Description: 自定义过滤器调用安全性元数据源
 * CreateDate:  2021/1/13 17:47
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    public static final String AT = "@";

    /**
     * 元数据资源地址缓存前缀
     */
    private static final String METADATA_RESOURCE_ADDRESS_CACHE_PREFIX = "metadata.resource-address";

    /**
     * 客户访问范围
     */
    private static final String CLIENT_ACCESS_SCOPE = "client-access-scope";

    /**
     * 客户权限
     */
    private static final String CLIENT_AUTHORITY = "client-authority";

    /**
     * 用户权限
     */
    private static final String USER_AUTHORITY = "user-authority";

    /**
     * 配置属性的前缀: 客户端访问范围
     * {@link ConfigAttribute}
     */
    public static final String CONFIG_ATTR_PREFIX_CLIENT_ACCESS_SCOPE = CLIENT_ACCESS_SCOPE + AT;

    /**
     * 配置属性的前缀: 客户端职权
     * {@link ConfigAttribute}
     */
    public static final String CONFIG_ATTR_PREFIX_CLIENT_AUTHORITY = CLIENT_AUTHORITY + AT;

    /**
     * 配置属性的前缀: 用户端职权
     * {@link ConfigAttribute}
     */
    public static final String CONFIG_ATTR_PREFIX_USER_AUTHORITY = USER_AUTHORITY + AT;

    /**
     * 资源服务ID
     */
    private String resourceId;


    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        log.info("自定义过滤器调用安全性元数据源:{}", object);
        final FilterInvocation filterInvocation = (FilterInvocation) object;
        final String endpoint = filterInvocation.getRequestUrl();
        //TODO 从缓存中获取：客户访问范围、客户授权、用户权限
        // ~ 为 AccessDecisionManager 提供包含匹配当前访问的资源端点的 ClientAuthority, UserAuthority, 以及 ClientAccessScope 的集合
        //   格式:
        //       - ClientAccessScope: ClientAccessScope.CACHE_PREFIX@ClientAccessScopeName
        //       - ClientAuthority: ClientAuthority.CACHE_PREFIX@ClientAuthorityName
        //       - UserAuthority: UserAuthority.CACHE_PREFIX@UserAuthorityName
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    /**
     * 获取该SecurityMetadataSource对象中保存的针对所有安全对象的权限信息的集合。该方法的主要目的是被AbstractSecurityInterceptor用于启动时校验每个ConfigAttribute对象。
     *
     * @return null/抛出异常
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        log.debug("CustomFilterInvocationSecurityMetadataSource :: getAllConfigAttributes");
        throw new UnsupportedOperationException("不支持的操作!");
    }

    /**
     * 这里clazz表示安全对象的类型，该方法用于告知调用者当前SecurityMetadataSource是否支持此类安全对象，只有支持的时候，才能对这类安全对象调用getAttributes方法。
     *
     * @param clazz 安全对象类
     * @return true：对安全对象类调用getAttributes方法。
     */
    @Override
    public boolean supports(Class<?> clazz) {
        log.debug("CustomFilterInvocationSecurityMetadataSource :: supports :: {}", clazz.getCanonicalName());
        // ~ FilterInvocation: 持有与 HTTP 过滤器相关的对象
        log.warn("HTTP 过滤相关的对象:{}", FilterInvocation.class.isAssignableFrom(clazz));
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    /**
     * 设置资源服务器 ID
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    //资源权限认证例子
   /* @Autowired
    MenuService menuService;
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        List<Menu> menus = menuService.getAllMenusWithRole();
        for (Menu menu : menus) {
            if (antPathMatcher.match(menu.getUrl(), requestUrl)) {
                List<Role> roles = menu.getRoles();
                String[] str = new String[roles.size()];
                for (int i = 0; i < roles.size(); i++) {
                    str[i] = roles.get(i).getName();
                }
                return SecurityConfig.createList(str);
            }
        }
        return SecurityConfig.createList("ROLE_LOGIN");
    }*/
}
