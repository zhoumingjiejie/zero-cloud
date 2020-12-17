package com.github.icezerocat.zeroclient.web.controller;

import com.github.icezerocat.zeroopenfeign.build.FeignBuild;
import com.github.icezerocat.zeroopenfeign.client.service.ClientLineFeignService;
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

    private ClientLineFeignService clientLineFeignService;

    public FeignExController() {
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
        String s = null;
        s = s.concat(null);
        return "hello world".concat(new Date().toString()).concat(s);
    }


    /**
     * feign两种调用方式，兼容性
     *
     * @return 调用时间
     */
    @GetMapping("twoFeign")
    public String twoFeign() {
        String sendMessage = this.clientLineFeignService.sendMessage("我要兼容");
        return "twoFeign:" + new Date().toString() + "\t" + sendMessage;
    }
}
