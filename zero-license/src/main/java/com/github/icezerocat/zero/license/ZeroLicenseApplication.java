package com.github.icezerocat.zero.license;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 0.0.0
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.github.icezerocat.zero.license")
public class ZeroLicenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroLicenseApplication.class, args);
    }

}
