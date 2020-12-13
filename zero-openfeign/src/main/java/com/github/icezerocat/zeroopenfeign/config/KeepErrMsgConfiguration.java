package com.github.icezerocat.zeroopenfeign.config;

import com.alibaba.fastjson.JSON;
import com.github.icezerocat.zerocommon.exception.ApiException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * Description: 自定义error decoder，[进入熔断]，保留原始报错信息，向上层抛出
 * CreateDate:  2020/12/12 14:05
 *
 * @author zero
 * @version 1.0
 */
public class KeepErrMsgConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new UserErrorDecoder();
    }

    /**
     * 自定义错误
     */
    public static class UserErrorDecoder implements ErrorDecoder {
        private Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public Exception decode(String methodKey, Response response) {
            Exception exception = null;
            try {
                // 获取原始的返回内容
                String json = Util.toString(response.body().asReader());
                exception = new RuntimeException(json);
                logger.error("UserErrorDecoder:{}", json);
                ApiException apiException = JSON.parseObject(json, ApiException.class);
                // 业务异常包装成 HystrixBadRequestException，不进入熔断逻辑
                if (apiException != null) {
                    exception = apiException;
                }
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
            }
            return exception;
        }
    }
}
