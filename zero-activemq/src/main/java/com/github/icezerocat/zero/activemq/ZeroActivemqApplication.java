package com.github.icezerocat.zero.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

/**
 * @author 0.0.0
 */
@EnableJms
@SpringBootApplication
public class ZeroActivemqApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroActivemqApplication.class, args);
    }

}
