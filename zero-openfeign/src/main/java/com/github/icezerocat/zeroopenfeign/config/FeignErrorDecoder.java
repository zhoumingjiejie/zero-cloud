package com.github.icezerocat.zeroopenfeign.config;

import com.alibaba.fastjson.JSON;
import com.github.icezerocat.zeroopenfeign.exception.ExceptionInfo;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Description: Feign客户端发生http请求层面的错误——回调
 * CreateDate:  2020/11/29 23:31
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            if (response.body() != null) {
                // 这里直接拿到我们抛出的异常信息
                String message = Util.toString(response.body().asReader());
                log.error("FeignErrorDecoder:{}", message);
                ExceptionInfo exceptionInfo = JSON.parseObject(message, ExceptionInfo.class);
                // 业务异常包装成 HystrixBadRequestException，不进入熔断逻辑
                return new HystrixBadRequestException(exceptionInfo.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("FeignErrorDecoder数据处理异常：{}", e.getMessage());
            return new InternalException(e.getMessage());
        }
        return new InternalException("system error");
        //return decode(methodKey, response);
    }
}
