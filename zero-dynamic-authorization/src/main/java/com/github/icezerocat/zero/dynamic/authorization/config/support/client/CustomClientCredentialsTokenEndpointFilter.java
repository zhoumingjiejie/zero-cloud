package com.github.icezerocat.zero.dynamic.authorization.config.support.client;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Description: OAuth2 令牌端点的过滤器和身份验证端点。如果作为安全过滤器包含在内，则允许客户端使用请求参数进行身份验证
 * CreateDate:  2021/7/23 9:33
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
public class CustomClientCredentialsTokenEndpointFilter extends ClientCredentialsTokenEndpointFilter {
    public CustomClientCredentialsTokenEndpointFilter(
            PasswordEncoder passwordEncoder,
            ClientDetailsService clientDetailsService,
            AuthenticationEntryPoint authenticationEntryPoint
    ) {
        //只允许post请求
        super.setAllowOnlyPost(true);
        //认证失败处理类
        super.setAuthenticationEntryPoint(authenticationEntryPoint);
        //认证管理器
        super.setAuthenticationManager(new ClientAuthenticationManager(passwordEncoder, clientDetailsService));
        //后设置属性
        super.afterPropertiesSet();
    }

    /**
     * 身份认证处理器，处理认证请求{@link Authentication}
     */
    private static class ClientAuthenticationManager implements AuthenticationManager {

        private final PasswordEncoder passwordEncoder;

        private final ClientDetailsService clientDetailsService;

        public ClientAuthenticationManager(PasswordEncoder passwordEncoder, ClientDetailsService clientDetailsService) {
            this.passwordEncoder = passwordEncoder;
            this.clientDetailsService = clientDetailsService;
        }

        /**
         * @param authentication {"authenticated":false,"authorities":[],"credentials":"client-a-p","name":"client-a","principal":"client-a"}
         * @see AuthenticationManager#authenticate(Authentication)
         */
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            log.debug("Incoming Authentication (link-ClientCredentialsTokenEndpointFilter): {}", JSON.toJSONString(authentication));

            //客户端ID和明细
            final String clientId = authentication.getName();
            final ClientDetails clientDetails = this.clientDetailsService.loadClientByClientId(clientId);

            //凭据和密码比对
            if (!this.passwordEncoder.matches((CharSequence) authentication.getCredentials(), clientDetails.getClientSecret())) {
                throw new BadCredentialsException("客户端密码错误!");
            }

            return new ClientAuthenticationToken(clientDetails);
        }
    }

    /**
     * 客户端token创建
     */
    private static class ClientAuthenticationToken extends AbstractAuthenticationToken {

        private final Object principal;

        private final Object credentials;

        public ClientAuthenticationToken(ClientDetails clientDetails) {
            super(clientDetails.getAuthorities());
            this.principal = clientDetails.getClientId();
            this.credentials = clientDetails.getClientSecret();
            super.setAuthenticated(true);
        }

        @Override
        public Object getCredentials() {
            return credentials;
        }

        @Override
        public Object getPrincipal() {
            return principal;
        }
    }
}
