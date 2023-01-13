package com.yuechu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@SpringBootApplication
@ServletComponentScan
public class ReegieApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReegieApplication.class, args);
        log.info("outset sucess...");
    }

}
