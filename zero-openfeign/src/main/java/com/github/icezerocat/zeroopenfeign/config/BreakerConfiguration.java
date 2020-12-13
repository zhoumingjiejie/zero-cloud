package com.github.icezerocat.zeroopenfeign.config;

import com.alibaba.fastjson.JSON;
import com.github.icezerocat.zerocommon.exception.ApiException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

/**
 * Description: 自定义error decoder，[进入熔断]，保留原始报错信息，向上层抛出
 * CreateDate:  2020/12/12 14:05
 *
 * @author zero
 * @version 1.0
 */
public class BreakerConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new BreakerFeignDecoder();
    }

    /**
     * 自定义错误
     */
    public static class BreakerFeignDecoder implements ErrorDecoder {
        private Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public Exception decode(String methodKey, Response response) {
            Exception exception = new ApiException("BreakerFeignDecoderException:Feign接口暂时不可用");
            try {
                // 获取原始的返回内容
                String json = Util.toString(response.body().asReader());
                exception = new RuntimeException(json);
                logger.error("BreakerFeignDecoder:{}", json);
                // 业务异常包装成 HystrixBadRequestException，不进入熔断逻辑
                exception = JSON.parseObject(json, ApiException.class);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                ex.printStackTrace();
            }
            return exception;
        }
    }
}
