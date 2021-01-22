package com.github.icezerocat.resource.accesscontrol;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * Description: 配置安全拦截过滤器
 * {@link FilterSecurityInterceptor}
 * CreateDate:  2021/1/21 20:24
 *
 * @author zero
 * @version 1.0
 */
public class FilterSecurityInterceptorPostProcessor implements ObjectPostProcessor<FilterSecurityInterceptor> {

    private final AccessDecisionManager accessDecisionManager;

    private final FilterInvocationSecurityMetadataSource securityMetadataSource;

    public FilterSecurityInterceptorPostProcessor(AccessDecisionManager accessDecisionManager, FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.accessDecisionManager = accessDecisionManager;
        this.securityMetadataSource = securityMetadataSource;
    }

    @Override
    public <GenericFilterSecurityInterceptor extends FilterSecurityInterceptor> GenericFilterSecurityInterceptor postProcess(
            GenericFilterSecurityInterceptor filterSecurityInterceptor
    ) {
        filterSecurityInterceptor.setSecurityMetadataSource(securityMetadataSource);
        filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager);
        return filterSecurityInterceptor;
    }
}