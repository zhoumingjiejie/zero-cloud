package com.github.icezerocat.zeroclient2.web.controller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.icezerocat.zeroclient2.service.FeignService;
import github.com.icezerocat.core.aop.Timer;
import github.com.icezerocat.core.http.HttpResult;
import github.com.icezerocat.core.utils.DateUtil;
import github.com.icezerocat.mybatismp.common.mybatisplus.NoahServiceImpl;
import github.com.icezerocat.mybatismp.service.BaseMpBuildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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
@RequiredArgsConstructor
public class Client2Controller {

    @Value("${api.themeUrl:}")
    private String themeUrl;

    private final BaseMpBuildService baseMpBuildService;
    private final FeignService feignService;
    //private final ClientFeignService clientFeignService;

    /**
     * sendMessage
     *
     * @return 获取发送的消息
     */
    @RequestMapping("sendMessage")
    public HttpResult sendMessage(@RequestParam("message") String message) {
        return HttpResult.ok("客户端2：".concat(message).concat(DateUtil.formatDateTime(new Date())));
    }

    /**
     * say
     *
     * @return 请求信息
     */
    @GetMapping("say")
    public HttpResult say() {
        NoahServiceImpl<BaseMapper<Object>, Object> dynamicVersion = this.baseMpBuildService.newInstance("dynamic_version");
        log.debug("{}", dynamicVersion.list());
        //dynamicVersion.saveOrUpdateBatch(new ArrayList<Object>());
        return HttpResult.ok("客户端2：".concat(DateUtil.formatDateTime(new Date())));
    }

    /**
     * feign动态调用
     *
     * @return string
     */
    @GetMapping("feignLine")
    public HttpResult<String> feignLine() {
        return HttpResult.ok(this.feignService.clientSay());
    }

    /**
     * feign普通调用
     *
     * @return string
     */
    @GetMapping("feign")
    public HttpResult<String> feign() {
        return HttpResult.ok(this.feignService.clientSay());
    }

    /**
     * value加载时间段
     *
     * @return value
     */
    @Timer(description = "sayValue加注解")
    @GetMapping("sayValue")
    public HttpResult<String> sayValue() {
        log.debug("value:{}", this.themeUrl);
        return HttpResult.ok(this.themeUrl);
    }
}
