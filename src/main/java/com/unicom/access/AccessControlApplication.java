package com.unicom.access;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author mrChen
 * @date 2021/1/30 10:36
 */
@SpringBootApplication
@EnableAsync
public class AccessControlApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccessControlApplication.class, args);
    }

}
