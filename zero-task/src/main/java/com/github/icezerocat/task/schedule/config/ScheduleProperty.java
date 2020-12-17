package com.github.icezerocat.task.schedule.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.List;

/**
 * Description: TODO
 * CreateDate:  2020/12/17 14:35
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
@ConfigurationProperties(prefix = "schedule")
public class ScheduleProperty {

    /**
     * 扫描的包
     */
    private List<String> scanBasePackages;
}
