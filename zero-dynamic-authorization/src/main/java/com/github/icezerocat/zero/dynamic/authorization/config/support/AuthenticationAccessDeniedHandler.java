package com.github.icezerocat.zero.dynamic.authorization.config.support;


import com.github.icezerocat.zero.dynamic.authorization.config.support.response.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ProjectName: [icezero-system]
 * Package:     [com.raymon.system.admin.config.RestAuthenticationAccessDeniedHandler]
 * Description: 权限不足处理类 返回403
 * CreateDate:  2020/4/18 0:31
 *
 * @author 0.0.0
 * @version 1.0
 */
@Slf4j
@Component
public class AuthenticationAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        log.debug("AccessDeniedHandler triggered with exception: {}.", e.getClass().getCanonicalName());
        ResponseWrapper.forbiddenResponse(httpServletResponse, "请求: " + httpServletRequest.getRequestURI() + " 权限不足，无法访问系统资源.");
    }
}
