package com.github.icezerocat.zeroopenfeign.client.service;

import com.github.icezerocat.zeroopenfeign.constant.FeignClientName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description: 客户端2
 * CreateDate:  2021/3/2 10:24
 *
 * @author zero
 * @version 1.0
 */
@FeignClient(name = FeignClientName.ZERO_CLIENT2, path = FeignClientName.ZERO_CLIENT2)
public interface Client2FeignService {

    /**
     * sendMessage
     *
     * @param message 消息
     * @return 字符串
     */
    @PostMapping("/client2/sendMessage")
    String sendMessage(@RequestParam String message);
}
