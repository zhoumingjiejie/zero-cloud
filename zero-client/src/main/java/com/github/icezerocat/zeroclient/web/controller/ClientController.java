package com.github.icezerocat.zeroclient.web.controller;

import com.github.icezerocat.zeroopenfeign.client.service.ClientFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private ClientFeignService clientFeignService;

    /**
     * say
     *
     * @return result
     */
    @ApiOperation("say")
    @GetMapping("say")
    public String say() {
        this.clientFeignService.say();
        return "hello world".concat(new Date().toString());
    }

    /**
     * sendMessage
     *
     * @return result
     */
    @ApiOperation("sendMessage")
    @PostMapping("sendMessage")
    public String sendMessage(@RequestParam("message") String message) {
        return "hello world".concat(new Date().toString()).concat(message);
    }
}
