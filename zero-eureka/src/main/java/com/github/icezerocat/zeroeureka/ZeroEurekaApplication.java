package com.github.icezerocat.zeroeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author 0.0.0
 */
@EnableEurekaServer
@SpringBootApplication
public class ZeroEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroEurekaApplication.class, args);
    }

}
