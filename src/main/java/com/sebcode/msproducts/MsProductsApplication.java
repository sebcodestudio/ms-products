package com.sebcode.msproducts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MsProductsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsProductsApplication.class, args);
    }

}
