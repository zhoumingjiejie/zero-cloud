package com.github.icezerocat.webchar.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.Data;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.context.annotation.Bean;

/**
 * Description: 微信配置
 * CreateDate:  2021/10/25 17:41
 *
 * @author zero
 * @version 1.0
 */
@Data
//@Configuration
//@ConfigurationProperties("wechat")
public class WxConfig {

    private String appid;
    private String appSecret;
    private String mchId;
    private String mchKey;
    private String keyPath;

    @Bean
    public WxMpConfigStorage initStorage() {
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(appid);
        config.setSecret(appSecret);
        return config;
    }

    @Bean
    public WxMpService initWxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(initStorage());
        return wxMpService;
    }

    @Bean
    public WxPayService initWxPayService() {
        WxPayService wxPayService = new WxPayServiceImpl();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appid);
        payConfig.setMchId(mchId);
        payConfig.setMchKey(mchKey);
        payConfig.setKeyPath(keyPath);
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}
