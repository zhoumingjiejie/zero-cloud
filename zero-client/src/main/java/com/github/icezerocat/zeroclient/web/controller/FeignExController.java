package com.github.icezerocat.zeroclient.web.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Description: feign异常控制器
 * CreateDate:  2020/12/8 14:25
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("feignEx")
public class FeignExController {
    /**
     * say
     *
     * @return result
     */
    @ApiOperation("say")
    @GetMapping("say")
    public String say() {
        String s = null;
        s = s.concat(null);
        return "hello world".concat(new Date().toString()).concat(s);
    }
}
