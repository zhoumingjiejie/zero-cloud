package com.github.icezerocat.zero.consul;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 0.0.0
 */
@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class ZeroConsulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroConsulApplication.class, args);
        log.debug("代码千万行,注释第一行");
    }

}
