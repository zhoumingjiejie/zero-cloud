package com.github.icezerocat.zeroopenfeign.client.service;

import com.github.icezerocat.zeroopenfeign.config.BreakerConfiguration;
import com.github.icezerocat.zeroopenfeign.constant.FeignClientName;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Description: 客户端服务接口
 * CreateDate:  2020/12/7 18:02
 *
 * @author zero
 * @version 1.0
 */
@FeignClient(
        name = FeignClientName.ZERO_CLIENT,
        configuration = BreakerConfiguration.class,
        path = FeignClientName.ZERO_CLIENT
)
public interface ClientFeignService {

    /**
     * say
     *
     * @return 字符串
     */
    @GetMapping("/feignEx/say")
    String say();

    /**
     * sendMessage
     *
     * @param message 消息
     * @return 字符串
     */
    @PostMapping("POST client/sendMessage")
    String sendMessage(@Param("message") String message);
}
