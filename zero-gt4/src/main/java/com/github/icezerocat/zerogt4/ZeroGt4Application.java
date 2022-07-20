package com.github.icezerocat.zerogt4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 0.0.0
 */
@Slf4j
@SpringBootApplication
public class ZeroGt4Application {

    public static void main(String[] args) {
        SpringApplication.run(ZeroGt4Application.class, args);
        log.debug("代码千万行，注释第一行");
    }

}
