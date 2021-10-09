package com.github.icezerocat.zero.dynamic.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 0.0.0
 */
@SpringBootApplication(scanBasePackages = {"com.github.icezerocat"})
public class ZeroDynamicResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroDynamicResourceApplication.class, args);
    }

}
