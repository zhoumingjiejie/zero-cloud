package com.github.icezerocat.zeroopenfeign.client.service;

import com.github.icezerocat.zeroopenfeign.config.NotBreakerConfiguration;
import com.github.icezerocat.zeroopenfeign.config.ClientLineServiceFallback;
import com.github.icezerocat.zeroopenfeign.constant.FeignClientName;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Description: 客户端服务接口
 * CreateDate:  2020/12/7 18:02
 *
 * @author zero
 * @version 1.0
 */
@FeignClient(
        name = FeignClientName.ZERO_CLIENT_LINE,
        fallbackFactory = ClientLineServiceFallback.class,
        configuration = NotBreakerConfiguration.class
)
public interface ClientLineFeignService {

    /**
     * say
     *
     * @return 字符串
     */
    @RequestLine("GET /say")
    String say();

    /**
     * sendMessage
     *
     * @param message 消息
     * @return 字符串
     */
    @RequestLine("POST /sendMessage?message={message}")
    String sendMessage(@Param("message") String message);
}
