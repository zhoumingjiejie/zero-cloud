package com.github.icezerocat.zeroclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * Description: swagger配置参数属性
 * CreateDate:  2020/10/15 17:27
 *
 * @author zero
 * @version 1.0
 */
@Configuration
@PropertySources({@PropertySource(value = "classpath:bootstrap.yml", ignoreResourceNotFound = true), @PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true), @PropertySource(value = "classpath:application.yml", ignoreResourceNotFound = true)})
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    /**
     * models扫描包
     */
    private String[] basePackages;

    /**
     * Swagger json url address
     */
    private String swaggerUrl;

    /**
     * 自定义url请求后缀
     */
    private String urlSuffix;

    public String[] getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public void setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix;
    }

    public String getSwaggerUrl() {
        return swaggerUrl;
    }

    public void setSwaggerUrl(String swaggerUrl) {
        this.swaggerUrl = swaggerUrl;
    }
}
