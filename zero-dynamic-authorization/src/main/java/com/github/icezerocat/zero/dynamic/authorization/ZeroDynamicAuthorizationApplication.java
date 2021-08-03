package com.github.icezerocat.zero.dynamic.authorization;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 0.0.0
 */
@Slf4j
@MapperScan({"com.github.icezerocat.*.mapper", "com.github.icezerocat.zero.dynamic.authorization.repository"})
@SpringBootApplication(scanBasePackages = {"com.github.icezerocat"})
public class ZeroDynamicAuthorizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroDynamicAuthorizationApplication.class, args);
        log.debug("代码千万行，注释第一行");
    }

}
