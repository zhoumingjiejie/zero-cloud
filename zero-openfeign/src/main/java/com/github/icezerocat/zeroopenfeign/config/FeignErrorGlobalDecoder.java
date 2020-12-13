package com.github.icezerocat.zeroopenfeign.config;

import com.alibaba.fastjson.JSON;
import com.github.icezerocat.zerocommon.exception.ApiException;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Description: Feign客户端发生http请求层面的错误——回调
 * CreateDate:  2020/11/29 23:31
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Configuration
public class FeignErrorGlobalDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        Exception exception = new ApiException("FeignErrorGlobalDecoder:Feign接口暂时不可用");
        try {
            if (response.body() != null) {
                // 这里直接拿到我们抛出的异常信息
                String json = Util.toString(response.body().asReader());
                log.error("FeignErrorDecoder:{}", json);
                ApiException exceptionInfo = JSON.parseObject(json, ApiException.class);
                // 业务异常包装成 HystrixBadRequestException，不进入熔断逻辑
                if (exceptionInfo != null) {
                    exception = new HystrixBadRequestException(json);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("FeignErrorDecoder数据处理异常：{}", e.getMessage(), e);
        }
        return exception;
    }
}
