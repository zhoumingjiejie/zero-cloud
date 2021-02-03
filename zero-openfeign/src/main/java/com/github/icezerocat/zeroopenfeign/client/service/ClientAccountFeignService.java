package com.github.icezerocat.zeroopenfeign.client.service;

import com.github.icezerocat.zeroopenfeign.constant.FeignClientName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Description: account-Feign
 * CreateDate:  2021/1/30 10:04
 *
 * @author zero
 * @version 1.0
 */
@FeignClient(name = FeignClientName.ZERO_CLIENT_ACCOUNT, path = FeignClientName.ZERO_CLIENT_ACCOUNT)
public interface ClientAccountFeignService {

    /**
     * 插入用户数据
     */
    @GetMapping("/account/insert")
    void insert();
}
