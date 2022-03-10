package com.github.icezerocat.icezerocat.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableJpaRepositories(basePackages = { "com.github.icezerocat.icezerocat.jpa.repository" })
@SpringBootApplication(scanBasePackages = "com.github.icezerocat")
public class IcezerocatJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(IcezerocatJpaApplication.class, args);
    }

}
