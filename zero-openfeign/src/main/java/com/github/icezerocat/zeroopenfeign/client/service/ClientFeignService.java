package com.github.icezerocat.zeroopenfeign.client.service;

import com.github.icezerocat.zeroopenfeign.constant.FeignClientName;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

import java.net.URI;

/**
 * Description: 客户端服务接口
 * CreateDate:  2020/12/7 18:02
 *
 * @author zero
 * @version 1.0
 */
@FeignClient(name = FeignClientName.ZERO_CLIENT)
public interface ClientFeignService {

    /**
     * say
     *
     * @return 字符串
     */
    @RequestLine("GET /say")
    String say();

    /**
     * say
     *
     * @param uri 请求url
     * @return 字符串
     */
    @RequestLine("GET /say")
    String say(URI uri);
}
