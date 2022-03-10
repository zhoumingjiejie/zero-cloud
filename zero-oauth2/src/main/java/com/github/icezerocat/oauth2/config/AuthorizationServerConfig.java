package com.github.icezerocat.oauth2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

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

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;


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
                //.checkTokenAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()")
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
        clients
                //使用内存存储客户端信息
                .inMemory()
                //客户端ID
                .withClient("client")
                //密钥
                .secret(this.passwordEncoder.encode("secret"))
                //此客户端允许的授权类型：密码式：password,隐藏式：implicit,授权码：authorization_code,客户端凭证：client_credentials
                .authorizedGrantTypes("authorization_code", "implicit", "password", "client_credentials", "refresh_token")
                //.authorizedGrantTypes("authorization_code", "implicit", "refresh_token")
                //权限
                .scopes("app", "admin", "user")
                //回调uri，在authorization_code与implicit授权方式时，用以接收服务器的返回信息
                .redirectUris("http://www.baidu.com")
                //登录后绕过批准询问(/oauth/confirm_access)
                .autoApprove(true);

    }

    /**
     * 配置授权以及令牌的访问端点和令牌服务，令牌发放
     *
     * @param endpoints 端点
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(this.tokenStore())
                .approvalStore(this.approvalStore())
                .authenticationManager(this.authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .userDetailsService(this.userDetailsService)
        //reuseRefreshTokens设置为false时，每次通过refresh_token获得access_token时，也会刷新refresh_token；也就是说，会返回全新的access_token与refresh_token。
        //默认值是true，只返回新的access_token，refresh_token不变。
        //.reuseRefreshTokens(false)
        ;
    }

    @Bean
    public TokenStore tokenStore() {
        //token保存在内存中（也可以保存在数据库、Redis中）。
        //如果保存在中间件（数据库、Redis），那么资源服务器与认证服务器可以不在同一个工程中。
        //注意：如果不保存access_token，则没法通过access_token取得用户信息
        return new InMemoryTokenStore();
    }

    @Bean
    public ApprovalStore approvalStore() {
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(this.tokenStore());
        return store;
    }
}
