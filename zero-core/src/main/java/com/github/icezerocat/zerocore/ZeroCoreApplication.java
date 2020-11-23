package com.github.icezerocat.zerocore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 0.0.0
 */
@SpringBootApplication(scanBasePackages = "com.github.icezerocat")
public class ZeroCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroCoreApplication.class, args);
    }

}
