package com.trendyTracker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.trendyTracker.service.TechService;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@EnableJpaAuditing
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class TrendyTrackerApplication implements CommandLineRunner{
    private final TechService techService;

    public static void main(String[] args) {
        SpringApplication.run(TrendyTrackerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("get TechList!.");
        techService.getTechList();
    }
}
