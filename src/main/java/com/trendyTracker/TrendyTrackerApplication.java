package com.trendyTracker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;

import com.trendyTracker.Job.service.RecruitService;
import com.trendyTracker.Job.service.TechService;

import lombok.RequiredArgsConstructor;

@SpringBootApplication   // Spring boot
@EnableJpaAuditing       // JPA
@EnableAspectJAutoProxy  // AOP
@EnableKafka             // Kafka
@RequiredArgsConstructor
public class TrendyTrackerApplication implements CommandLineRunner{
    private final TechService techService;
    private final RecruitService recruitService;

    public static void main(String[] args) {
        SpringApplication.run(TrendyTrackerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("init get TechList.");
        techService.getTechList();

        System.out.println("init get total job cnt.");
        recruitService.getTotalJobCnt();
    }
}
