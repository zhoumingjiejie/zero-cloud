package com.github.icezerohub.zero.dynamic.resource.config.support.accesscontrol;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * Description:
 * 1、安全拦截过滤器(全局安全拦截，设置accessDecisionManager、securityMetadataSource)
 * 2、SecurityMetadataSource（安全元数据资源） 为设置accessDecisionManager（访问决策控制）提供判断依据 (ConfigAttribute).
 * 【借助 ObjectPostProcessor 机制, 用户才有机会配置 FilterSecurityInterceptor 这样的拦截器.】
 * {@link FilterSecurityInterceptor}
 * CreateDate:  2021/8/10 20:33
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
