package com.kkumta.timedeal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TimedealApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TimedealApplication.class, args);
    }
    
}