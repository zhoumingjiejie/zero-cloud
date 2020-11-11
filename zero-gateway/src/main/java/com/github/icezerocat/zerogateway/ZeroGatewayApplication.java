package com.github.icezerocat.zerogateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 0.0.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ZeroGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroGatewayApplication.class, args);
    }

}
