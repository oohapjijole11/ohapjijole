package com.sparta.final_project.config.security;

import com.sparta.final_project.domain.aop.Distributed;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DistributedConfig {

    @Bean
    public Distributed getDistributedAop() {
        return new Distributed();
    }


}
