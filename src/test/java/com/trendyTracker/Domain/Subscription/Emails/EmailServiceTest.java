package com.trendytracker.Domain.Subscription.Emails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.trendytracker.Adaptors.CacheMemory.EmailValidationCacheImpl;
import com.trendytracker.Domain.Subscription.Emails.EmailService;
import com.trendytracker.Domain.Subscription.Emails.Vo.EmailValidation;

import jakarta.mail.internet.MimeMessage;

public class EmailServiceTest {
    
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private MimeMessage mockMimeMessage;
    @Mock
    private EmailValidationCacheImpl redisManager;

    private EmailService emailService;

    private final String email = "test@example.com";

    @BeforeEach
    void setUp() throws JsonMappingException, JsonProcessingException {
        MockitoAnnotations.openMocks(this);
        when(mailSender.createMimeMessage()).thenReturn(mockMimeMessage);

        emailService = new EmailService(mailSender, redisManager);
    }

    @Test
    void testSendVerificationEmail() throws Exception {
        // when 
        String verificationCode = emailService.sendVerificationEmail(email);
        verify(mailSender).send(mockMimeMessage);

        // then
        assertNotNull(verificationCode);
        assertEquals(6, verificationCode.length());
    }

    @Test
    void testVerifyCode() throws Exception {
        // given 
        String verificationCode = emailService.sendVerificationEmail(email);
        var emailValidation = new EmailValidation(email, verificationCode);
        when(redisManager.getEmailValidation(email)).thenReturn(Optional.of(emailValidation));

        // when 
        boolean isCodeValid = emailService.verifyCode(email, verificationCode);
        
        // then
        assertTrue(isCodeValid);
        boolean isCodeInvalid = emailService.verifyCode(email, "wrongcode");
        assertFalse(isCodeInvalid);
    }
}
