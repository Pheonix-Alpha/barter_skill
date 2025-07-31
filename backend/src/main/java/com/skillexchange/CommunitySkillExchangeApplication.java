package com.skillexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 👈 ADD THIS to enable background scheduled tasks
public class CommunitySkillExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunitySkillExchangeApplication.class, args);
    }

}
