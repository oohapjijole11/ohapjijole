package com.sparta.final_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@EnableCaching
@EnableJpaAuditing
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class FinalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalProjectApplication.class, args);
    }

}
