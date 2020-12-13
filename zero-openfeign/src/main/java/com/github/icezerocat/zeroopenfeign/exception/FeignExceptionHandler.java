package com.github.icezerocat.zeroopenfeign.exception;

import com.alibaba.fastjson.JSON;
import com.github.icezerocat.zerocommon.exception.ApiException;
import com.github.icezerocat.zerocommon.http.HttpResult;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Description: feign异常助手
 * CreateDate:  2020/12/13 16:41
 *
 * @author zero
 * @version 1.0
 */
@RestControllerAdvice
public class FeignExceptionHandler {

    private Logger log = LoggerFactory.getLogger(FeignExceptionHandler.class);

    /**
     * 运行异常
     *
     * @param e 自定义异常
     * @return code
     */
    @ExceptionHandler({HystrixBadRequestException.class})
    public HttpResult feignErrorHandler(HystrixBadRequestException e) {
        log.error("feign异常：{}", e.getMessage());
        e.printStackTrace();
        return HttpResult.Build.getInstance()
                .setCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMessage("响应失败:" + e.getMessage())
                .setData(JSON.parseObject(e.getMessage(), ApiException.class))
                .complete();
    }
}
