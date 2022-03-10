package com.github.icezerocat.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
     * 身份验证管理器生成器
     *
     * @param auth 身份验证管理器生成器
     * @throws Exception 认证异常
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //用户信息保存在内存中
        //在鉴定角色roler时，会默认加上ROLLER_前缀
        auth
                .inMemoryAuthentication()
                .withUser("user").password(this.passwordEncoder().encode("user")).roles("USER")
                .and()
                .withUser("test").password(this.passwordEncoder().encode("test")).roles("TEST")
                .and()
                .withUser("nicky").password("{noop}123").roles("admin");
    }

    /**
     * （登录页设置），（匿名用户放行，动态权限拦截，受保护请求）
     *
     * @param http Http安全
     * @throws Exception 异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin() //登记界面，默认是permit All
                .and()
                //不用身份认证可以访问
                .authorizeRequests().antMatchers("/", "/home", "/oauth/**").permitAll()
                .and()
                //其它的请求要求必须有身份认证
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .csrf() //防止CSRF（跨站请求伪造）配置
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize")).disable();
    }

    /**
     * 认证管理器：防止无法自动注入 and 如果不配置SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 用户详细信息服务
     *
     * @return 默认详细信息服务
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return super.userDetailsService();
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
