package com.github.icezerocat.oauth2.config;

import com.github.icezerocat.oauth2.security.SecurityOauthAccessDecisionManager;
import com.github.icezerocat.oauth2.security.SecurityOauthMetadataSource;
import com.github.icezerocat.oauth2.service.SecurityOauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * Description: 资源服务配置
 * CreateDate:  2021/1/11 11:49
 *
 * @author zero
 * @version 1.0
 */
@Configuration
@EnableResourceServer
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final TokenStore tokenStore;
    private final SecurityOauthService securityOauthService;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                .tokenStore(this.tokenStore)
                .resourceId("resourceId");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        /*
    	 注意：默认所有的资源都受保护
    	 1、必须先加上： .requestMatchers().antMatchers(...)，表示对资源进行保护，也就是说，在访问前要进行OAuth认证。
    	 2、接着：访问受保护的资源时，要具有哪里权限。
    	 ------------------------------------
    	 否则，请求只是被Security的拦截器拦截，请求根本到不了OAuth2的拦截器。
    	 同时，还要注意先配置：security.oauth2.resource.filter-order=3，否则通过access_token取不到用户信息。
    	 ------------------------------------
    	 requestMatchers()部分说明：
    	 Invoking requestMatchers() will not override previous invocations of ::
    	 mvcMatcher(String)}, requestMatchers(), antMatcher(String), regexMatcher(String), and requestMatcher(RequestMatcher).
    	 */
        http
                // Since we want the protected resources to be accessible in the UI as well we need
                // session creation to be allowed (it's disabled by default in 2.0.6)
                //另外，如果不设置，那么在通过浏览器访问被保护的任何资源时，每次是不同的SessionID，并且将每次请求的历史都记录在OAuth2Authentication的details的中
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests().antMatchers("/oauth/**").permitAll()
                .and()
                .requestMatchers()
                .antMatchers("/user", "/res/**")
                .and()
                .authorizeRequests()
                .antMatchers("/user", "/res/**")
                .authenticated()
                .and().authorizeRequests()
                // 自定义FilterInvocationSecurityMetadataSource
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(
                            O fsi) {
                        fsi.setSecurityMetadataSource(new SecurityOauthMetadataSource(fsi.getSecurityMetadataSource(), securityOauthService));
                        fsi.setAccessDecisionManager(securityOauthAccessDecisionManager());
                        return fsi;
                    }
                })
        ;
    }

    @Bean
    public SecurityOauthAccessDecisionManager securityOauthAccessDecisionManager() {
        return new SecurityOauthAccessDecisionManager();
    }
}
