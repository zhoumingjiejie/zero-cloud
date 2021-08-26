package com.github.icezerocat.zero.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ProjectName: [icezero-system]
 * Package:     [com.raymon.system.admin.config.SecurityProperties]
 * Description: Security数据资源
 * CreateDate:  2020/4/29 15:37
 *
 * @author 0.0.0
 * @version 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "tencent.sms")
public class SmsProperties {

    /**
     * id
     */
    private String secretId;
    /**
     * key
     */
    private String secretKey;
    /**
     * 端点
     */
    private String endpoint;
    /**
     *
     */
    private String sdkAppId;
    /**
     * 短信签名内容
     */
    private String signName;
    /**
     * 模板ID
     */
    private String templateId;
    /**
     * 发件人ID
     */
    private String senderId;
}
