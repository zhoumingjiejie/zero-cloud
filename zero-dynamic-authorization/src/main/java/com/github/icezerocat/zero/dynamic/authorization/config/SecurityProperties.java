package com.github.icezerocat.zero.dynamic.authorization.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

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
@ConfigurationProperties(prefix = "security-matchers")
public class SecurityProperties {
    private List<String> list = new ArrayList<>();
}
