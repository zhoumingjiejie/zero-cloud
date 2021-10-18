package com.github.icezerocat.zero.dynamic.resource.config.support;

import com.github.icezerocat.zero.dynamic.resource.config.response.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 自定义的 {@link org.springframework.security.web.AuthenticationEntryPoint}
 * CreateDate:  2021/10/9 10:23
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Component
public class CustomResourceAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private WebResponseExceptionTranslator<?> exceptionTranslator = new DefaultWebResponseExceptionTranslator();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        //解析异常，如果是401则处理
        ResponseEntity<?> result = null;
        try {
            result = exceptionTranslator.translate(authException);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null && result.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            log.debug("自动刷新请求：\n" +
                    "1.通过exceptionTranslator.translate(authException)解析异常，判断异常类型（status）\n" +
                    "2.如果不是401异常，则直接调用默认异常处理器的处理方法即可\n" +
                    "3.如果是401异常则向授权服务器发起token刷新的请求\n" +
                    "4.如果token刷新成功，则通过request.getRequestDispatcher(request.getRequestURI()).forward(request,response);再次请求资源\n" +
                    "5.如果token刷新失败，要么跳转到登陆页面（web的话也可以通过response.sendirect跳转到登陆页面），要么返回错误信息（json）");
        }
        log.debug("Custom AuthenticationEntryPoint triggered with exception: {}.", authException.getClass().getCanonicalName());
        ResponseWrapper.forbiddenResponse(response, authException.getMessage());
    }

}
