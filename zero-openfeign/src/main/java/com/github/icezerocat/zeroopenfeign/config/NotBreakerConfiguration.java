package com.github.icezerocat.zeroopenfeign.config;

import com.alibaba.fastjson.JSON;
import com.github.icezerocat.zerocommon.exception.ApiException;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * Description: 自定义error decoder，不进入熔断，保留原始报错信息，向上层抛出
 * CreateDate:  2020/12/12 6:57
 *
 * @author zero
 * @version 1.0
 */
public class NotBreakerConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new SpecialErrorDecoder();
    }

    /**
     * 自定义错误
     */
    public static class SpecialErrorDecoder implements ErrorDecoder {
        private Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public Exception decode(String methodKey, Response response) {
            Exception exception = null;
            try {
                String json = Util.toString(response.body().asReader());
                exception = new RuntimeException(json);
                logger.error("NotBreakerConfiguration:{}", json);
                ApiException apiException = JSON.parseObject(json, ApiException.class);
                // 业务异常包装成 HystrixBadRequestException，不进入熔断逻辑
                if (apiException != null) {
                    exception = new HystrixBadRequestException(json);
                }
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
            }
            return exception;
        }
    }
}
