package com.github.icezerocat.zeroclient2;

import com.github.icezerocat.zerocore.config.ApplicationContextZeroHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 0.0.0
 */
@SpringBootApplication
@ComponentScan(
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ApplicationContextZeroHelper.class)},
        basePackages = {"com.github.icezerocat", "github.com.icezerocat"}
)
@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients("com.github.icezerocat")
@MapperScan("com.github.icezerocat.**.mapper")
public class ZeroClient2Application {

    public static void main(String[] args) {
        SpringApplication.run(ZeroClient2Application.class, args);
    }

}
