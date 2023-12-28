package com.trendyTracker.Domain.Subscription.Emails;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.trendyTracker.Domain.Subscription.Emails.Vo.EmailValidation;

@Repository
public interface EmailValidationCache {
    void storeEmailValidation(EmailValidation emailValidation) throws JsonProcessingException;

    Optional<EmailValidation> getEmailValidation(String email) throws JsonMappingException, JsonProcessingException;

    void deleteKey(String key);
}
