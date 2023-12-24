package com.trendyTracker.Domain.Subscription.Emails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class EmailServiceTest {
    
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private MimeMessage mockMimeMessage;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mailSender.createMimeMessage()).thenReturn(mockMimeMessage);
        emailService = new EmailService(mailSender);
    }

    @Test
    void testSendVerificationEmail() throws MessagingException {
        // given
        String email = "test@example.com";

        // when 
        String verificationCode = emailService.sendVerificationEmail(email);
        verify(mailSender).send(mockMimeMessage);

        // then
        assertNotNull(verificationCode);
        assertEquals(6, verificationCode.length());
    }

    @Test
    void testVerifyCode() throws MessagingException {
        // given 
        String email = "test@example.com";
        String verificationCode = emailService.sendVerificationEmail(email);

        // when 
        boolean isCodeValid = emailService.verifyCode(email, verificationCode);
        
        // then
        assertTrue(isCodeValid);
        boolean isCodeInvalid = emailService.verifyCode(email, "wrongcode");
        assertFalse(isCodeInvalid);
    }
}
