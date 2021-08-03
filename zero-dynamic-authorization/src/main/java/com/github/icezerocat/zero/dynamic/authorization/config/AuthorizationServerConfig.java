package com.github.icezerocat.zero.dynamic.authorization.config;

import com.github.icezerocat.zero.dynamic.authorization.config.support.AuthenticationAccessDeniedHandler;
import com.github.icezerocat.zero.dynamic.authorization.config.support.CustomAuthenticationEntryPoint;
import com.github.icezerocat.zero.dynamic.authorization.config.support.client.CustomClientCredentialsTokenEndpointFilter;
import com.github.icezerocat.zero.dynamic.authorization.config.support.token.CustomAuthorizationServerTokenServices;
import com.github.icezerocat.zero.dynamic.authorization.config.support.token.CustomTokenGranter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import javax.annotation.Resource;

/**
 * Description:  授权服务器配置类<br>
 * {@code @EnableAuthorizationServer} 会启用 {@link org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint}
 * 和 {@link org.springframework.security.oauth2.provider.endpoint.TokenEndpoint} 端点.
 * CreateDate:  2021/7/22 17:17
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * Authorization Code Grant 的获取授权码的端点
     */
    public static final String OAUTH_AUTHORIZE_ENDPOINT = "/oauth/authorize";

    final private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    final private AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;

    @Resource(name = "defaultAuthenticationManager")
    private AuthenticationManager authenticationManager;

    @Resource(name = "customClientDetailsService")
    private ClientDetailsService clientDetailsService;

    @Resource(name = "customUserDetailsService")
    private UserDetailsService userDetailsService;

    /**
     * 密码编码器
     *
     * @return 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 端点的安全约束
     *
     * @param security 安全
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // @formatter:off
        // ~ 为 client_id 和 client_secret 开启表单验证, 会启用一个名为 ClientCredentialsTokenEndpointFilter 的过滤器.
        //   并会把这个过滤器放在 BasicAuthenticationFilter 之前,
        //   这样如果在 ClientCredentialsTokenEndpointFilter 完成了校验 (SecurityContextHolder.getContext().getAuthentication()),
        //   且请求头中即使有 Authorization: basic xxx, 也会被 BasicAuthenticationFilter 忽略.
        //   ref: AuthorizationServerSecurityConfigurer#clientCredentialsTokenEndpointFilter, BasicAuthenticationFilter#doFilterInternal
        // ~ 如果不配置这一行, 默认就会通过 BasicAuthenticationFilter.
        // security.allowFormAuthenticationForClients();

        security
                // ~ Applied by AuthorizationServerSecurityConfiguration#configure(HttpSecurity)

                // ~ endpoint /oauth/token_key access control, 获取公钥的端点
                .tokenKeyAccess("permitAll()")
                // ~ endpoint /oauth/check_token access control, 供资源服务器校验令牌的端点 (如果采用的是 RemoteTokenServices)
                .checkTokenAccess("isAuthenticated()")
                // 权限不足处理类(用来解决认证过的用户访问无权限资源时的异常)
                .accessDeniedHandler(this.authenticationAccessDeniedHandler)
                // 认证失败处理类(用来解决匿名用户访问无权限资源时的异常),在 Client Credentials Grant 和 Resource Owner Password Grant 模式下, 客户端凭证有误时会触发
                .authenticationEntryPoint(this.customAuthenticationEntryPoint)
                //为 /oauth/token 端点 (TokenEndpoint) 添加自定义的过滤器
                .addTokenEndpointAuthenticationFilter(new CustomClientCredentialsTokenEndpointFilter(this.passwordEncoder(), this.clientDetailsService, this.customAuthenticationEntryPoint));
    }

    /**
     * 客户端详情服务
     *
     * @param clients 客户
     * @throws Exception 异常
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(this.clientDetailsService);
    }

    /**
     * 配置授权以及令牌的访问端点和令牌服务，令牌发放
     * 授权服务器的安全配置, 主要是配置 {@code oauth/token} 端点.
     *
     * @param endpoints 端点
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //对于密码授权模式, 需要提供 AuthenticationManager 用于用户信息的认证
                .authenticationManager(this.authenticationManager)
                // ~ refresh_token required
                .userDetailsService(this.userDetailsService)

                //自定义的 TokenStore
                .tokenStore(this.tokenStore())
                // 自定义的 Token增强器
                .tokenEnhancer(this.jwtAccessTokenConverter())
                // ~ 自定义的 TokenGranter
                .tokenGranter(new CustomTokenGranter(endpoints, this.authenticationManager))
                // ~ 自定义的 AuthorizationServerTokenServices
                .tokenServices(new CustomAuthorizationServerTokenServices(endpoints));
    }

    /**
     * Description: 自定义token商店 {@link JwtTokenStore}
     *
     * @return org.springframework.security.oauth2.provider.token.TokenStore {@link JwtTokenStore}
     */
    private TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * Description: Jwt 访问令牌转换器 {@link JwtTokenStore} 所需要
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
