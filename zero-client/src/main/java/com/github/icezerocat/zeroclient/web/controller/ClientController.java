package com.github.icezerocat.zeroclient.web.controller;

import com.github.icezerocat.zeroopenfeign.build.FeignBuild;
import com.github.icezerocat.zeroopenfeign.client.service.ClientFeignService;
import com.github.icezerocat.zeroopenfeign.client.service.ClientLineFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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

    private final ClientFeignService clientFeignService;
    private ClientLineFeignService clientLineFeignService;

    public ClientController(ClientFeignService clientFeignService) {
        this.clientFeignService = clientFeignService;
        this.clientLineFeignService = FeignBuild.getFeignClient(ClientLineFeignService.class, "http://127.0.0.1:10003/zero-client/client");
    }

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

    /**
     * feign两种调用方式，兼容性
     *
     * @return 调用时间
     */
    @GetMapping("twoFeign")
    public String twoFeign() {
        String sendMessage = this.clientLineFeignService.sendMessage("我要兼容同一个地方");
        return "twoFeign:" + new Date().toString() + "\t" + sendMessage;
    }
}
