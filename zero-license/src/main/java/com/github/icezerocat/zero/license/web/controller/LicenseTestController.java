package com.github.icezerocat.zero.license.web.controller;

import com.github.icezerocat.component.license.verify.annotion.VLicense;
import com.github.icezerocat.component.license.verify.config.LicenseVerifyProperties;
import com.github.icezerocat.zero.license.config.LicenseVerifyProperties2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description: 认证测试控制器
 * CreateDate:  2021/9/1 15:38
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("test")
public class LicenseTestController {
    @Value("${springboot.license.verify.isGlobalLicense}")
    private boolean isGlobalLicense;
    @Value("${springboot.license.verify.storePass}")
    private String storePass;

    @Resource
    private LicenseVerifyProperties licenseVerifyProperties;
    @Resource
    private LicenseVerifyProperties2 licenseVerifyProperties2;

    @GetMapping("say")
    public String say() {
        log.debug("全局拦截：{}", isGlobalLicense);
        log.debug("全局拦截：{}", storePass);
        log.debug("全局拦截：{}", licenseVerifyProperties.isGlobalLicense());
        log.debug("全局拦截：{}", licenseVerifyProperties2.isGlobalLicense());
        return "hello";
    }

    @VLicense
    @GetMapping("sayLicense")
    public String sayLicense() {
        return "license";
    }
}
