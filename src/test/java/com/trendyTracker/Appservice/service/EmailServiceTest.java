package com.trendyTracker.Appservice.service;

import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import jakarta.mail.MessagingException;

@SpringBootTest
public class EmailServiceTest {
    @MockBean
    private EmailService emailService;


    @BeforeEach
    void tempEmailList() throws MessagingException{
        String email = "wlstncjs1234@naver.com";
        when(emailService.sendVerificationEmail(email)).thenReturn("123456");

        when(emailService.verifyCode(email, "123456")).thenReturn(true);
    }

    @Test
    @DisplayName("이메일 인증코드 발송 테스트")
    void testSendVerificationEmail() throws MessagingException {
        // given 
        String email = "wlstncjs1234@naver.com";

        // when 
        String sendVerificationEmail = emailService.sendVerificationEmail(email);

        // then 
        Assertions.assertThat(sendVerificationEmail.length()).isEqualTo(6);
    }

    @Test
    @DisplayName("이메일 인증코드 검증")
    void testVerifyCode() throws MessagingException{
        // given 
        String email = "wlstncjs1234@naver.com";
        String code = emailService.sendVerificationEmail(email);

        // when 
        Boolean verifyCode = emailService.verifyCode(email, code);

        // then
        Assertions.assertThat(verifyCode).isTrue();
    }
}
