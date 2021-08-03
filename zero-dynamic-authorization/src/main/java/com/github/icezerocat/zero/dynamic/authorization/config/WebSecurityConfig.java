package com.github.icezerocat.zero.dynamic.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;

import javax.annotation.Resource;

/**
 * Description: 安全认证配置
 * CreateDate:  2021/1/11 11:47
 *
 * @author zero
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 默认的登陆端点
     */
    public static final String DEFAULT_LOGIN_URL = "/login";

    /**
     * 用户认证提供者
     */
    @Resource(name = "usernamePasswordAuthenticationProvider")
    private AuthenticationProvider authenticationProvider;

    /**
     * 认证管理器：防止无法自动注入 and 如果不配置SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
     * {@link org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#authenticationManagerBean()}
     *
     * @throws Exception 认证管理器注入异常
     */
    @Bean("defaultAuthenticationManager")
    public AuthenticationManager defaultAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    /**
     * 用户验证管理
     *
     * @param auth 认证
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.authenticationProvider);
    }

    /**
     * http安全请求
     *
     * @param http http
     * @throws Exception 异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // ~ 为客户端，JWT-token
        http.csrf().disable();
        // ~ Authorization Code Grant 和 Implicit Grant 需要开启表单登陆, 设置默认页面登陆过滤器
        http.formLogin().and().addFilter(this.defaultLoginPageGeneratingFilter());
        // ~ 禁用 Authorization: Basic xxx
        http.httpBasic().disable();
        // 其它的请求要求必须有身份认证
        http.authorizeRequests().anyRequest().authenticated();
    }

    /**
     * Description: 用默认的登陆界面生成过滤器生成默认的登陆界面 /login.<br>
     * Details: 如果使用了自定义的 {@link com.github.icezerocat.zero.dynamic.authorization.config.support.CustomAuthenticationEntryPoint}, {@link DefaultLoginPageGeneratingFilter} 就不会被配置, 所以这里需要手动配置.
     *
     * @see DefaultLoginPageGeneratingFilter
     * @see org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer
     */
    private DefaultLoginPageGeneratingFilter defaultLoginPageGeneratingFilter() {
        final DefaultLoginPageGeneratingFilter defaultLoginPageGeneratingFilter =
                new DefaultLoginPageGeneratingFilter(new UsernamePasswordAuthenticationFilter());
        defaultLoginPageGeneratingFilter.setAuthenticationUrl(DEFAULT_LOGIN_URL);
        return defaultLoginPageGeneratingFilter;
    }
}
