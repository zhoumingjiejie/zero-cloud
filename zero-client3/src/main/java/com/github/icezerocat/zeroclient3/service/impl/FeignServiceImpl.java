package com.github.icezerocat.zeroclient3.service.impl;

import com.github.icezerocat.zeroclient3.service.FeignService;
import com.github.icezerocat.zeroopenfeign.client.service.ClientFeignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Description: feign服务Impl
 * CreateDate:  2020/12/8 18:15
 *
 * @author zero
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class FeignServiceImpl implements FeignService {

    private final ClientFeignService clientFeignService;

    @Override
    public String clientSay() {
        return null;
    }
}
