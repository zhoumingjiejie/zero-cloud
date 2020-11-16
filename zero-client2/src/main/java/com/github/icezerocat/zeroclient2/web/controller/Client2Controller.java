package com.github.icezerocat.zeroclient2.web.controller;

import github.com.icezerocat.core.http.HttpResult;
import github.com.icezerocat.core.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Description: 客户端2控制器
 * CreateDate:  2020/11/13 11:22
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("client2")
public class Client2Controller {


    /**
     * say
     *
     * @return 请求信息
     */
    @GetMapping("say")
    public HttpResult say() {
        return HttpResult.ok("客户端2：".concat(DateUtil.formatDateTime(new Date())));
    }
}
