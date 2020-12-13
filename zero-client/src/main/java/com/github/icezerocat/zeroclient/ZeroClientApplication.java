package com.github.icezerocat.zeroclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 0.0.0
 */
@EnableFeignClients("com.github.icezerocat")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.github.icezerocat")
public class ZeroClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroClientApplication.class, args);
    }

}
