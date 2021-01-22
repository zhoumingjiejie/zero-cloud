package com.github.icezerocat.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
     * 认证提供者
     * <p>
     * {@link com.github.icezerocat.authorization.security.UsernamePasswordAuthenticationProvider}
     */
    private AuthenticationProvider authenticationProvider;

    @Autowired
    public void setAuthenticationProvider(@Qualifier("usernamePasswordAuthenticationProvider") AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * 用户验证管理
     *
     * @param auth 认证
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
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
        // ~ Authorization Code Grant 和 Implicit Grant 需要开启表单登陆
        http.formLogin();
        // ~ 禁用 Authorization: Basic xxx
        http.httpBasic().disable();
        // 其它的请求要求必须有身份认证
        http.authorizeRequests().anyRequest().authenticated();
    }

    /**
     * 认证管理器：防止无法自动注入 and 如果不配置SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
     *
     * @throws Exception 认证管理器注入异常
     */
    @Bean
    public AuthenticationManager defaultAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    /**
     * 密码编码器
     *
     * @return 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
