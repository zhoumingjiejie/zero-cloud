package com.github.icezerocat.zeroopenfeign.config;

import com.github.icezerocat.zeroopenfeign.factory.InfoFeignLoggerFactory;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignLoggerFactory;
import org.springframework.cloud.openfeign.support.PageableSpringEncoder;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
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

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Autowired(required = false)
    private SpringDataWebProperties springDataWebProperties;

    @Bean
    Logger.Level feignLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    FeignLoggerFactory infoFeignLoggerFactory() {
        return new InfoFeignLoggerFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public Decoder feignDecoder() {
        return new OptionalDecoder(
                new ResponseEntityDecoder(new SpringDecoder(this.messageConverters)));
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnMissingClass("org.springframework.data.domain.Pageable")
    public Encoder feignEncoder() {
        return new SpringEncoder(this.messageConverters);
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.data.domain.Pageable")
    @ConditionalOnMissingBean
    public Encoder feignEncoderPageable() {
        PageableSpringEncoder encoder = new PageableSpringEncoder(
                new SpringEncoder(this.messageConverters));
        if (springDataWebProperties != null) {
            encoder.setPageParameter(
                    springDataWebProperties.getPageable().getPageParameter());
            encoder.setSizeParameter(
                    springDataWebProperties.getPageable().getSizeParameter());
            encoder.setSortParameter(
                    springDataWebProperties.getSort().getSortParameter());
        }
        return encoder;
    }

}
