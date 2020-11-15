package com.github.icezerocat.zeroclient.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;

/**
 * Description: 客户端控制器
 * CreateDate:  2020/11/11 16:29
 *
 * @author zero
 * @version 1.0
 */
@ApiIgnore
@Slf4j
@Api("客户端控制器")
@RestController
@RequestMapping("client")
public class ClientController {

    /**
     * say
     *
     * @return result
     */
    @ApiOperation("say")
    @GetMapping("say")
    public String say() {
        return "hello world".concat(new Date().toString());
    }
}
