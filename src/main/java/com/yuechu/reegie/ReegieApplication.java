package com.yuechu.reegie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ReegieApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReegieApplication.class, args);
        log.info("outset sucess...");
    }

}
