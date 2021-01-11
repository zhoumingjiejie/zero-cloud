package com.github.icezerocat.oauth.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: hello控制器
 * CreateDate:  2021/1/7 11:18
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
public class HelloController {

    @GetMapping("admin/say")
    public String say() {
        String say = "hello admin";
        log.debug(say);
        return say;
    }
}
