package com.github.icezerocat.zerogateway.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 默认熔断控制器
 * CreateDate:  2020/11/11 15:19
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
public class DefaultHystrixController {

    /**
     * 熔断默认返回信息
     *
     * @return 信息
     */
    @RequestMapping("/defaultfallback")
    public Map<String, String> defaultfallback() {
        log.debug("降级操作...");
        Map<String, String> map = new HashMap<>();
        map.put("code", "501");
        map.put("message", "服务异常");
        map.put("data", "null");
        return map;
    }
}
