package com.github.icezerocat.zeroopenfeign.config;

import com.github.icezerocat.zeroopenfeign.factory.InfoFeignLoggerFactory;
import feign.Logger;
import org.springframework.cloud.openfeign.FeignLoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: feign日志配置
 * CreateDate:  2020/11/16 9:41
 *
 * @author zero
 * @version 1.0
 */
@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    FeignLoggerFactory infoFeignLoggerFactory() {
        return new InfoFeignLoggerFactory();
    }
}
