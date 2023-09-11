package com.trendyTracker.Appservice.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.trendyTracker.TrendyTrackerApplication;

import jakarta.mail.MessagingException;

@SpringBootTest(classes = TrendyTrackerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmailServiceTest {
    @Autowired
    private EmailService emailService;

    @Test
    void testSendVerificationEmail() throws MessagingException {
        // given 
        String email = "wlstncjs1234@naver.com";

        // when 
        String sendVerificationEmail = emailService.sendVerificationEmail(email);

        // then 
        Assertions.assertThat(sendVerificationEmail.length()).isEqualTo(6);
    }
}
