package com.github.icezerocat.zeroopenfeign.factory;

import feign.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignLoggerFactory;

/**
 * Description: feign日志工厂（info级别）
 * CreateDate:  2020/11/16 9:37
 *
 * @author zero
 * @version 1.0
 */
public class InfoFeignLoggerFactory implements FeignLoggerFactory {
    @Override
    public Logger create(Class<?> type) {
        return new InfoFeignLogger(LoggerFactory.getLogger(type));
    }
}
