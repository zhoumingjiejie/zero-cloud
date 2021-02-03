package com.github.icezerocat.clientaccount;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 0.0.0
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableAutoDataSourceProxy
@MapperScan("com.github.icezerocat.**.mapper")
public class ZeroClientAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroClientAccountApplication.class, args);
    }

}
