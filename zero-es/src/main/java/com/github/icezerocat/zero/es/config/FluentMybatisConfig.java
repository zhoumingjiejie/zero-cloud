package com.github.icezerocat.zero.es.config;

import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: fluent配置
 * CreateDate:  2022/4/20 17:16
 *
 * @author zero
 * @version 1.0
 */
@Configuration
public class FluentMybatisConfig {

    @Bean
    public MapperFactory mapperFactory() {
        return new MapperFactory();
    }

}
