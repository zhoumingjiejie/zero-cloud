package com.github.icezerocat.zeroclient3.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 同步属性
 * CreateDate:  2020/12/21 18:10
 *
 * @author zero
 * @version 1.0
 */
@Data
@Configuration
@PropertySources({
        @PropertySource(value = "classpath:bootstrap.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:bootstrap.yml", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:application.yml", ignoreResourceNotFound = true)
})
@ConfigurationProperties(prefix = "sync")
public class SyncProperty {

    /**
     * 监听的方法全名，需要处理数据的bean
     */
    private Map<String, String> methodMapBean = new HashMap<>();
}
