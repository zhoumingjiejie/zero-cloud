package com.github.icezerocat.zerofluentmp.config;

import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: fluent-myBatis配置
 * CreateDate:  2021/10/13 10:04
 *
 * @author zero
 * @version 1.0
 */
@Configuration
public class FluentMyBatisConfig {
    @Bean
    public MapperFactory mapperFactory() {
        return new MapperFactory();
    }
}
