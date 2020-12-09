package com.github.icezerocat.zeroclient2.service.Impl;

import com.github.icezerocat.zeroclient2.constant.FeignConstant;
import com.github.icezerocat.zeroclient2.service.FeignService;
import com.github.icezerocat.zeroopenfeign.build.FeignBuild;
import com.github.icezerocat.zeroopenfeign.client.service.ClientFeignService;
import feign.codec.Decoder;
import feign.codec.Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

/**
 * Description: feign服务impl
 * CreateDate:  2020/12/7 18:06
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Service
@Import(FeignClientsConfiguration.class)
public class FeignServiceImpl implements FeignService {

    private final ClientFeignService clientFeignService;

    @SuppressWarnings("all")
    public FeignServiceImpl(Decoder decoder, Encoder encoder) {
        String url = FeignConstant.url;
        this.clientFeignService = FeignBuild.getFeignClient(ClientFeignService.class, FeignConstant.url, decoder, encoder);
    }

    @Override
    public String clientSay() {
        log.debug("无参数调用：{}", this.clientFeignService.say());
        return this.clientFeignService.sendMessage(" hello world ！！！");
    }
}
