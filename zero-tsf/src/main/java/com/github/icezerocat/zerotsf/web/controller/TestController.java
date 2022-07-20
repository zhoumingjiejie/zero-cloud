package com.github.icezerocat.zerotsf.web.controller;

import com.github.icezerocat.zerotsf.config.ProviderNameConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 测试控制器
 * CreateDate:  2022/6/13 15:19
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController {

    private final ProviderNameConfig providerNameConfig;

    /**
     * 获取配置的属性
     *
     * @return 属性
     */
    @GetMapping("getName")
    public String getName() {
        log.debug("name:{}", this.providerNameConfig.getName());
        System.out.println(StringUtils.join("name:", this.providerNameConfig.getName()));
        return this.providerNameConfig.getName();
    }

    /**
     * say
     *
     * @return say
     */
    @GetMapping("say")
    public String say() {
        return "hello";
    }
}
