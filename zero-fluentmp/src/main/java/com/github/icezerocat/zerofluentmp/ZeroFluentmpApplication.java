package com.github.icezerocat.zerofluentmp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.github.icezerocat.zerofluentmp.mapper")
@SpringBootApplication
public class ZeroFluentmpApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroFluentmpApplication.class, args);
    }

}
