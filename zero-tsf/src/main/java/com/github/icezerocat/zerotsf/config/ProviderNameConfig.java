package com.github.icezerocat.zerotsf.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Description: 提供名字配置
 * CreateDate:  2022/6/13 11:36
 *
 * @author zero
 * @version 1.0
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "provider.config")
public class ProviderNameConfig {

    /**
     * 名字
     */
    private String name = "echo-provider-default-name";
}
