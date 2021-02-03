package com.github.icezerocat.clientorder;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 0.0.0
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableAutoDataSourceProxy
@EnableFeignClients("com.github.icezerocat")
@MapperScan("com.github.icezerocat.**.mapper")
public class ZeroClientOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroClientOrderApplication.class, args);
    }

}
