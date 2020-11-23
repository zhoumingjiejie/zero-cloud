package com.github.icezerocat.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 0.0.0
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.github.icezerocat")
public class ZeroTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroTaskApplication.class, args);
    }

}
