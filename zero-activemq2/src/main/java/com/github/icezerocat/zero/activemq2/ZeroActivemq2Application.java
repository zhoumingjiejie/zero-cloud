package com.github.icezerocat.zero.activemq2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

/**
 * @author 0.0.0
 */
@EnableJms
@SpringBootApplication
public class ZeroActivemq2Application {

    public static void main(String[] args) {
        SpringApplication.run(ZeroActivemq2Application.class, args);
    }

}
