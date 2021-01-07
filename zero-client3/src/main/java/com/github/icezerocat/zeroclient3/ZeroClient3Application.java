package com.github.icezerocat.zeroclient3;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 0.0.0
 */
@MapperScan("com.github.icezerocat.**.mapper")
@SpringBootApplication(scanBasePackages = {"com.github.icezerocat", "github.com.icezerocat"})
public class ZeroClient3Application {

    public static void main(String[] args) {
        SpringApplication.run(ZeroClient3Application.class, args);
    }

}
