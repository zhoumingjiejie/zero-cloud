package com.github.icezerocat.config.zeroconfiggateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 0.0.0
 */
@EnableFeignClients("com.github.icezerocat")
@EnableDiscoveryClient
@MapperScan("com.github.icezerocat.**.mapper")
@SpringBootApplication(scanBasePackages = "com.github.icezerocat")
public class ZeroConfigGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroConfigGatewayApplication.class, args);
    }

}
