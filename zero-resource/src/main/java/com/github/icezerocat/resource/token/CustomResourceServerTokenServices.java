package com.github.icezerocat.resource.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Description: 自定义资源服务tokenService
 * {@link ResourceServerTokenServices}
 * CreateDate:  2021/1/21 19:43
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
public class CustomResourceServerTokenServices implements ResourceServerTokenServices {
    /**
     * {@link JwtTokenStore}
     */
    private final TokenStore tokenStore;

    public CustomResourceServerTokenServices(JwtAccessTokenConverter accessTokenConverter) {
        this.tokenStore = new JwtTokenStore(accessTokenConverter);
    }

    /**
     * Description: 用于从 accessToken 中加载凭证信息, 并构建出 {@link OAuth2Authentication} 的方法
     *
     * @see ResourceServerTokenServices#loadAuthentication(String)
     */
    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        log.debug("CustomResourceServerTokenServices :: loadAuthentication called ...");
        log.trace("CustomResourceServerTokenServices :: loadAuthentication :: accessToken: {}", accessToken);

        // ~ 令牌过期判断
        final OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
        if (oAuth2AccessToken.isExpired()) {
            throw new InvalidTokenException("Token has expired!");
        }

        return tokenStore.readAuthentication(accessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        log.debug("CustomResourceServerTokenServices :: readAccessToken called ...");
        throw new UnsupportedOperationException("暂不支持 readAccessToken!");
    }
}
