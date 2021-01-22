package com.github.icezerocat.authorization.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * Description: 授权服务配置
 * CreateDate:  2021/1/11 11:48
 *
 * @author zero
 * @version 1.0
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 认证管理器
     * {@link WebSecurityConfig#authenticationManagerBean()}
     */
    private final AuthenticationManager authenticationManager;
    /**
     * 用户信息服务
     * {@link com.github.icezerocat.authorization.security.UserDetailsServiceImpl}
     */
    @Qualifier("userDetailsService")
    private final UserDetailsService userDetailsService;

    /**
     * 客户端信息服务
     * {@link com.github.icezerocat.authorization.security.ClientDetailsServiceImpl}
     */
    private ClientDetailsService clientDetailsService;

    @Autowired
    public void setClientDetailsService(@Qualifier("clientDetailsServiceImpl") ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    /**
     * 端点的安全约束
     *
     * @param security 安全
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                //允许所有资源服务器访问公钥端点（/oauth/token_key）
                .tokenKeyAccess("permitAll()")
                //只允许验证用户访问令牌解析端点（/oauth/check_token）
                .checkTokenAccess("isAuthenticated()")
                // 允许客户端发送表单来进行权限认证来获取令牌
                .allowFormAuthenticationForClients();
    }

    /**
     * 客户端详情服务
     *
     * @param clients 客户
     * @throws Exception 异常
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //添加客户端信息
        clients.withClientDetails(this.clientDetailsService);
    }

    /**
     * 配置授权以及令牌的访问端点和令牌服务，令牌发放
     * 授权服务器的安全配置, 主要是配置 {@code oauth/token} 端点.
     *
     * @param endpoints 端点
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(this.tokenStore())
                .authenticationManager(this.authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .userDetailsService(this.userDetailsService)
        //reuseRefreshTokens设置为false时，每次通过refresh_token获得access_token时，也会刷新refresh_token；也就是说，会返回全新的access_token与refresh_token。
        //默认值是true，只返回新的access_token，refresh_token不变。
        //.reuseRefreshTokens(false)
        ;
    }

    /**
     * Description: 自定义Jwt的token
     * {@link JwtTokenStore}
     *
     * @return org.springframework.security.oauth2.provider.token.TokenStore {@link JwtTokenStore}
     */
    private TokenStore tokenStore() {
        return new JwtTokenStore(this.jwtAccessTokenConverter());
    }

    /**
     * Description: Jwt Token转换器
     * 为 {@link JwtTokenStore} 所须
     *
     * @return org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
     */
    private JwtAccessTokenConverter jwtAccessTokenConverter() {
        final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("authorization-server.jks"), "SCLiKe11040218".toCharArray());
        final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair("authorization-server-jwt-keypair"));
        return jwtAccessTokenConverter;
    }
}
