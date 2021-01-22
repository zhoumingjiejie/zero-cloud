package com.github.icezerocat.oauth2.security;

import com.github.icezerocat.oauth2.service.SecurityOauthService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Description: Security鉴权的拓展
 * CreateDate:  2021/1/11 16:36
 *
 * @author zero
 * @version 1.0
 */
public class SecurityOauthMetadataSource implements FilterInvocationSecurityMetadataSource {

    private FilterInvocationSecurityMetadataSource superMetadataSource;

    private static SecurityOauthService securityOauthService;

    private static Map<String, List<ConfigAttribute>> configAttributeMap = null;

    private final AntPathMatcher matcher = new AntPathMatcher(File.separator);

    public SecurityOauthMetadataSource(FilterInvocationSecurityMetadataSource expressionBasedFilterInvocationSecurityMetadataSource, SecurityOauthService securityOauthService) {
        this.superMetadataSource = expressionBasedFilterInvocationSecurityMetadataSource;
        SecurityOauthMetadataSource.securityOauthService = securityOauthService;
    }


    public static void loadDataSource() {
        if (configAttributeMap == null) {
            configAttributeMap = securityOauthService.loadDataSource();
        }
    }

    public void clearDataSource() {
        configAttributeMap.clear();
        configAttributeMap = null;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        loadDataSource();
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();
        String path = url.split("\\?")[0];

        //排除公共资源
        if (matcher.match("/public/**", path) || "/file/getImage".equals(path)
                || "OPTIONS".equals(fi.getRequest().getMethod())
                || matcher.match("/swagger-ui.html", path)
                || matcher.match("/webjars/**", path)
                || matcher.match("/v2/**", path)
                || matcher.match("/swagger-resources", path)
                || matcher.match("/swagger-resources/**", path)
        ) {
            return SecurityConfig.createList("ROLE_ANONYMOUS", "ROLE_ADMIN", "ROLE_CLIENT");
        }
        if (configAttributeMap.get(path) != null) {
            return configAttributeMap.get(path);
        } else {
            return superMetadataSource.getAttributes(object);
        }
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
