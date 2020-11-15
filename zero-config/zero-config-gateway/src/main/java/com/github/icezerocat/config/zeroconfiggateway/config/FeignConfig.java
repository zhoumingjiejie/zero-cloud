package com.github.icezerocat.config.zeroconfiggateway.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: feign配置
 * CreateDate:  2020/11/15 19:40
 *
 * @author zero
 * @version 1.0
 */
@Configuration
public class FeignConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
