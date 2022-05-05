package com.github.icezerocat.zero.es;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 0.0.0
 */
@MapperScan("com.github.icezerocat")
@SpringBootApplication
public class ZeroEsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroEsApplication.class, args);
    }

}
