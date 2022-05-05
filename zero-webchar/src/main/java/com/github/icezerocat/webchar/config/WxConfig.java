package com.github.icezerocat.webchar.config;

import lombok.Data;
import me.chanjar.weixin.mp.config.WxMpHostConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * Description: 微信配置-支持代理转发
 * CreateDate:  2021/10/25 17:41
 *
 * @author zero
 * @version 1.0
 */
@Data
@Configuration
public class WxConfig implements CommandLineRunner {

    @Value("${webChar.apiUrl:}")
    private String apiUrl;

    @Value("${webChar.mpUrl:}")
    private String mpUrl;

    @Value("${webChar.openUrl:}")
    private String openUrl;


    @Override
    public void run(String... args) throws Exception {
        Optional.ofNullable(this.apiUrl).ifPresent(o -> WxMpHostConfig.API_DEFAULT_HOST_URL = o);
        Optional.ofNullable(this.mpUrl).ifPresent(o -> WxMpHostConfig.MP_DEFAULT_HOST_URL = o);
        Optional.ofNullable(this.openUrl).ifPresent(o -> WxMpHostConfig.OPEN_DEFAULT_HOST_URL = o);
    }
}
