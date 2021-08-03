package com.github.icezerocat.zero.dynamic.authorization.config.support;

import com.github.icezerocat.zero.dynamic.authorization.config.AuthorizationServerConfig;
import com.github.icezerocat.zero.dynamic.authorization.config.WebSecurityConfig;
import com.github.icezerocat.zero.dynamic.authorization.config.support.response.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description: 认证失败处理类{@link AuthenticationEntryPoint}
 * CreateDate:  2021/7/22 21:31
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final RequestMatcher authorizationCodeGrantRequestMatcher = new AuthorizationCodeGrantRequestMatcher();

    private final AuthenticationEntryPoint loginUrlAuthenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(WebSecurityConfig.DEFAULT_LOGIN_URL);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.debug("Custom AuthenticationEntryPoint triggered with exception: {}.", e.getClass().getCanonicalName());

        // 触发重定向到登陆页面
        if (authorizationCodeGrantRequestMatcher.matches(httpServletRequest)) {
            loginUrlAuthenticationEntryPoint.commence(httpServletRequest, httpServletResponse, e);
            return;
        }

        //忽略报错
        List<String> filterUrl = Arrays.asList("/error", "/csrf");
        String requestUri = httpServletRequest.getRequestURI();
        if (filterUrl.contains(requestUri)) {
            return;
        }

        e.printStackTrace();
        StringBuilder msg = new StringBuilder("请求访问: ");
        msg.append(requestUri).append(" 接口， 经jwt认证失败，无法访问系统资源.").append(e.getMessage());
        // 用户登录时身份认证未通过
        if (e instanceof BadCredentialsException) {
            log.info("用户登录时身份认证失败.{}", msg.toString());
            ResponseWrapper.unauthorizedResponse(httpServletResponse, msg.toString());
        } else if (e instanceof InsufficientAuthenticationException) {
            log.info("缺少请求头参数,Authorization传递是token值所以参数是必须的.{}", msg.toString());
            ResponseWrapper.unauthorizedResponse(httpServletResponse, msg.toString());
        } else {
            log.info("用户token无效.{}", msg.toString());
            ResponseWrapper.unauthorizedResponse(httpServletResponse, msg.toString());
        }

        //403无权访问，禁止响应
        ResponseWrapper.forbiddenResponse(httpServletResponse, e.getMessage());
    }

    /**
     * 授权码授予请求匹配器
     */
    private static class AuthorizationCodeGrantRequestMatcher implements RequestMatcher {

        /**
         * <ol>
         *     <li>授权码模式 URI</li>
         *     <li>隐式授权模式 URI</li>
         * </ol>
         */
        private static final Set<String> SUPPORT_URIS = new HashSet<>(Arrays.asList("response_type=code", "response_type=token"));

        @Override
        public boolean matches(HttpServletRequest request) {
            if (StringUtils.equals(request.getServletPath(), AuthorizationServerConfig.OAUTH_AUTHORIZE_ENDPOINT)) {
                final String queryString = request.getQueryString();
                return SUPPORT_URIS.stream().anyMatch(supportUri -> StringUtils.indexOf(queryString, supportUri) != StringUtils.INDEX_NOT_FOUND);
            }
            return false;
        }
    }
}
