package com.github.icezerocat.zeroclient2.config;

import com.github.icezerocat.zeroclient2.constant.FeignConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Description: 应用数据初始化：常量、数据字典
 * CreateDate:  2020/12/7 19:04
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Component
public class ApplicationConstruct {

    @Value("${api.themeUrl:}")
    private String themeUrl;

    /**
     * 出事feign请求的url
     */
    @PostConstruct
    public void initFeignUrl() {
        FeignConstant.url = this.themeUrl;
        log.info("feign调用地址(feign base url): {}", FeignConstant.url);
    }
}
