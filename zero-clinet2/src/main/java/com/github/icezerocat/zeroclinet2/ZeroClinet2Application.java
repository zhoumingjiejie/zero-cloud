package com.github.icezerocat.zeroclinet2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 0.0.0
 */
@MapperScan("com.github.icezerocat.**.mapper")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.github.icezerocat", "github.com.icezerocat"})
public class ZeroClinet2Application {

    public static void main(String[] args) {
        SpringApplication.run(ZeroClinet2Application.class, args);
    }

}
