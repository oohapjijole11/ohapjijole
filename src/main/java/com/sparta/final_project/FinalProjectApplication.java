package com.sparta.final_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Map;

@EnableCaching
@EnableJpaAuditing
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class FinalProjectApplication {
    public static void main(String[] args) {
        System.out.println("=== 환경 변수 ===");
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.println(envName + " = " + env.get(envName));
        }

        SpringApplication.run(FinalProjectApplication.class, args);
    }
}
