package com.pharmacie.pharmacie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication  
@EntityScan(basePackages = "com.pharmacie.pharmacie.model")
@EnableScheduling
public class PharmacieApplication {

    public static void main(String[] args) {
        SpringApplication.run(PharmacieApplication.class, args);
    }

}
