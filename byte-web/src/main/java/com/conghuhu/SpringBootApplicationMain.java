package com.conghuhu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author conghuhu
 * @create 2021-09-24 19:51
 */
@SpringBootApplication
@EnableScheduling
public class SpringBootApplicationMain {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationMain.class, args);
    }
}
