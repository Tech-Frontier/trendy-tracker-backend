package com.trendyTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TrendyTrackerApplication {
    // http://localhost:8080/swagger-ui/index.html

    public static void main(String[] args) {
        SpringApplication.run(TrendyTrackerApplication.class, args);
    }
}
