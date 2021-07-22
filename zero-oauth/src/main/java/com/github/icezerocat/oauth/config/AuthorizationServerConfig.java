package com.github.icezerocat.oauth.config;

import com.github.icezerocat.oauth.model.CustomTokenEnhancer;
import io.micrometer.core.instrument.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * Description: 授权服务配置
 * CreateDate:  2021/1/7 9:31
 *
 * @author zero
 * @version 1.0
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final DataSource dataSource;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //允许所有资源服务器访问公钥端点（/oauth/token_key）
        security.tokenKeyAccess("permitAll()");
        //只允许验证用户访问令牌解析端点（/oauth/check_token）
        security.checkTokenAccess("isAuthenticated()");
        // 允许客户端发送表单来进行权限认证来获取令牌
        security.allowFormAuthenticationForClients();

        //解决Encoded password does not look like BCrypt报错
        //因为springsecurity在最新版本升级后,默认把之前的明文密码方式给去掉了
        //https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released#password-storage-updated
        //security.passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //添加客户端信息
        clients
                //使用内存存储客户端信息
                .inMemory()
                //客户端ID
                .withClient("client")
                //密钥
                .secret(new BCryptPasswordEncoder().encode("secret"))
                //此客户端允许的授权类型：密码式：password,隐藏式：implicit,授权码：authorization_code,客户端凭证：client_credentials
                .authorizedGrantTypes("authorization_code", "implicit", "password", "client_credentials")
                //权限
                .scopes("app", "admin", "user")
                .redirectUris("http://www.baidu.com")
                //登录后绕过批准询问(/oauth/confirm_access)
                .autoApprove(true);
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(this.authenticationManager)
                //Token的生成方式
                .tokenStore(this.tokenStore())
                //令牌增强器
                .tokenEnhancer(this.tokenEnhancer())
        ;

        //token
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(endpoints.getTokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        //设置令牌增强器
        defaultTokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        //有效期30天
        defaultTokenServices.setAccessTokenValiditySeconds((int) TimeUtils.daysToUnit(30, TimeUnit.SECONDS));
        endpoints.tokenServices(defaultTokenServices);
    }

    /**
     * 声明 ClientDetails实现
     *
     * @return ClientDetailsService
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(this.dataSource);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 使用同一个密钥来编码 JWT 中的  OAuth2 令牌（对称加密）
     *
     * @return Jwt访问令牌转换器
     */
    /*@Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        String publicKey = "123";
        converter.setSigningKey(publicKey);
        //不设置这个会出现 Cannot convert access token to JSON
        converter.setVerifier(new RsaVerifier(publicKey));
        return converter;
    }*/

    /**
     * 使用私钥编码 JWT 中的  OAuth2 令牌（非对称加密）
     *
     * @return Jwt访问令牌转换器
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 私密文件和密码。
        org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory keyStoreKeyFactory
                = new org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory((new ClassPathResource("zero.jks")), "123456".toCharArray());
        //KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("zero.jks"), "123456".toCharArray());
        // 私密文件的别名
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("zero"));

        //公钥（读取public.txt的公钥信息）
        Resource resource = new ClassPathResource("public.txt");
        String publicKey;
        try {
            publicKey = inputStream2String(resource.getInputStream());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        converter.setVerifierKey(publicKey);
        //不设置这个会出现 Cannot convert access token to JSON
        //converter.setVerifier(new RsaVerifier(publicKey));
        return converter;
    }

    /**
     * 令牌增强器,添加自定义属性
     *
     * @return 令牌增强器
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    /**
     * io转字符串
     *
     * @param is 输入流
     * @return 字符串
     * @throws IOException IO处理异常
     */
    private String inputStream2String(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

}
