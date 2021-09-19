package com.github.icezerohub.zero.dynamic.resource.config;

import com.alibaba.fastjson.JSON;
import com.github.icezerohub.zero.dynamic.resource.config.support.accesscontrol.CustomAccessDecisionManager;
import com.github.icezerohub.zero.dynamic.resource.config.support.accesscontrol.CustomFilterInvocationSecurityMetadataSource;
import com.github.icezerohub.zero.dynamic.resource.config.support.accesscontrol.FilterSecurityInterceptorPostProcessor;
import com.github.icezerohub.zero.dynamic.resource.config.support.token.CustomResourceServerTokenServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Description: 资源服务配置
 * CreateDate:  2021/8/10 18:54
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    /**
     * 资源服务器保存的持有公钥的文件名
     */
    private static final String AUTHORIZATION_SERVER_PUBLIC_KEY_FILENAME = "authorization-server.pub";

    /**
     * 资源服务器 ID
     */
    public static final String RESOURCE_ID = "resource-server";

    /**
     * 授权服务器的 {@link org.springframework.security.oauth2.provider.endpoint.TokenKeyEndpoint} 供资源服务器请求授权服务器获取公钥的端点<br>
     * 在资源服务器中, 可以有两种方式获取授权服务器用于签名 JWT 的私钥对应的公钥:
     * <ol>
     *     <li>本地获取 (需要公钥文件)</li>
     *     <li>请求授权服务器提供的端点 (/oauth/token_key) 获取</li>
     * </ol>
     */
    private static final String AUTHORIZATION_SERVER_TOKEN_KEY_ENDPOINT_URL = "http://localhost:18957/token-customize-authorization-server/oauth/token_key";

    // =================================================================================================================

    /**
     * 访问决策管理
     */
    final private CustomAccessDecisionManager customAccessDecisionManager;

    /**
     * 元数据资源：用过安全拦截是，访问决策判断的元数据
     */
    final private CustomFilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;

    // ~ ResourceServerSecurityConfigurer configure
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        // @formatter:off
        resources.resourceId(RESOURCE_ID).stateless(true);

        // ~ 指定 ResourceServerTokenServices
        resources.tokenServices(new CustomResourceServerTokenServices(jwtAccessTokenConverter()));

        // @formatter:on
    }

    // ~ TokenStore

    /**
     * Description: 为签名验证和解析提供转换器<br>
     * Details: 看起来 {@link org.springframework.security.jwt.crypto.sign.RsaVerifier} 已经被标记为过时了, 究其原因, 似乎 Spring 已经发布了一个新的产品 Spring Authorization Server, 有空再研究.
     *
     * @see <a href="https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth 2.0 Migration Guide</a>
     * @see <a href="https://spring.io/blog/2020/04/15/announcing-the-spring-authorization-server">Announcing the Spring Authorization Server</a>
     * @see JwtAccessTokenConverter
     */
    @SuppressWarnings("deprecation")
    private JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setVerifier(new RsaVerifier(retrievePublicKey()));
        return jwtAccessTokenConverter;
    }

    /**
     * Description: 获取公钥 (Verifier Key)<br>
     * Details: 启动时调用
     *
     * @return java.lang.String
     * @author LiKe
     * @date 2020-07-22 11:45:40
     */
    private String retrievePublicKey() {
        final ClassPathResource classPathResource = new ClassPathResource(AUTHORIZATION_SERVER_PUBLIC_KEY_FILENAME);
        try (
                // ~ 先从本地取读取名为 authorization-server.pub 的公钥文件, 获取公钥
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()))
        ) {
            log.debug("{} :: Retrieve public key locally ...", RESOURCE_ID);
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            // ~ 如果本地没有, 则尝试通过授权服务器的 /oauth/token_key 端点获取公钥
            log.debug("{} :: 从本地获取公钥失败: {}, 尝试从授权服务器 /oauth/token_key 端点获取 ...", RESOURCE_ID, e.getMessage());
            final RestTemplate restTemplate = new RestTemplate();
            final String responseValue = restTemplate.getForObject(AUTHORIZATION_SERVER_TOKEN_KEY_ENDPOINT_URL, String.class);

            log.debug("{} :: 授权服务器返回原始公钥信息: {}", RESOURCE_ID, responseValue);
            return JSON.parseObject(JSON.parseObject(responseValue).getString("data")).getString("value");
        }
    }

    // =================================================================================================================

    // ~ HttpSecurity configure
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated()
                // ~ 动态权限设置
                .withObjectPostProcessor(new FilterSecurityInterceptorPostProcessor(this.customAccessDecisionManager, this.filterInvocationSecurityMetadataSource));
    }

}
