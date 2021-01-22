package com.github.icezerocat.authorization;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 0.0.0
 */
//@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.github.icezerocat.**.mapper")
public class ZeroAuthorizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroAuthorizationApplication.class, args);
    }

}
