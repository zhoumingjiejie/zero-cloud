package com.github.icezerocat.clientstorage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 0.0.0
 */
@EnableDiscoveryClient
@MapperScan("com.github.icezerocat.**.mapper")
@SpringBootApplication
public class ZeroClientStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroClientStorageApplication.class, args);
    }

}
