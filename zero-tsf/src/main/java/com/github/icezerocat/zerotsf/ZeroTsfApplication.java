package com.github.icezerocat.zerotsf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.tsf.annotation.EnableTsf;

/**
 * @author 0.0.0
 */
@Slf4j
@EnableTsf
@SpringBootApplication
public class ZeroTsfApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroTsfApplication.class, args);
        log.debug("代码千万行，注释第一行");
        System.out.println("代码千万行，注释第一行");
    }

}
