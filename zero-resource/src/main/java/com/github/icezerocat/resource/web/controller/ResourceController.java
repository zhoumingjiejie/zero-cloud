package com.github.icezerocat.resource.web.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: ResourceController
 * CreateDate:  2021/1/13 16:47
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/resource")
public class ResourceController {
    @GetMapping("/access")
    public String access() {
        final String securityContext = JSON.toJSONString(SecurityContextHolder.getContext());
        log.debug("resource-server accessed with security context: ");
        log.debug(securityContext);
        return securityContext;
    }
}
