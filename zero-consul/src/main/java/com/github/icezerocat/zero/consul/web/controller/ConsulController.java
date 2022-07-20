package com.github.icezerocat.zero.consul.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 领事控制器
 * CreateDate:  2022/6/13 21:15
 *
 * @author zero
 * @version 1.0
 */
@RefreshScope
@RestController
@RequestMapping("consul")
public class ConsulController {

    /**
     * 名字
     */
    @Value("${name}")
    private String name;

    /**
     * 测试
     *
     * @return 姓名结果
     */
    @GetMapping("demo")
    public String consulDemo() {
        return this.name;
    }

}
