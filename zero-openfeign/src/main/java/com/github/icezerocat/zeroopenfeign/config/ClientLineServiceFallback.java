package com.github.icezerocat.zeroopenfeign.config;

import com.github.icezerocat.zerocommon.http.HttpResult;
import com.github.icezerocat.zeroopenfeign.client.service.ClientLineFeignService;
import feign.hystrix.FallbackFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Description: 获取异常信息
 * CreateDate:  2020/12/12 7:10
 *
 * @author zero
 * @version 1.0
 */
@Component
public class ClientLineServiceFallback implements FallbackFactory<ClientLineFeignService> {
    private static final Logger LOG = LoggerFactory.getLogger(ClientLineServiceFallback.class);
    private static final String ERR_MSG = "Test接口暂时不可用: ";

    @Override
    public ClientLineFeignService create(Throwable throwable) {
        String msg = throwable == null ? "" : throwable.getMessage();
        if (!StringUtils.isEmpty(msg)) {
            LOG.error(msg);
        }
        return new ClientLineFeignService() {
            @Override
            public String say() {
                return HttpResult.error(ERR_MSG + msg).toString();
            }

            @Override
            public String sendMessage(String message) {
                return HttpResult.error(ERR_MSG + msg).toString();
            }
        };
    }
}
