package com.github.icezerocat.config.zeroconfiggateway.web.controller;

import com.github.icezerocat.config.zeroconfiggateway.schedule.DynamicRouteScheduling;
import com.github.icezerocat.zerocommon.http.HttpResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description: 首页控制器
 * CreateDate:  2020/11/14 1:08
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final DynamicRouteScheduling dynamicRouteScheduling;

    /**
     * 首页
     *
     * @return 返回index首页
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 更新路由信息
     *
     * @return 更新结果
     */
    @GetMapping("update")
    @ResponseBody
    public HttpResult<String> update() {
        return this.dynamicRouteScheduling.update();
    }
}
