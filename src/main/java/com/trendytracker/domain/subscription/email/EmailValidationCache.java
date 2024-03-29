package com.trendytracker.domain.subscription.email;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.trendytracker.domain.subscription.email.vo.EmailValidation;

@Repository
public interface EmailValidationCache {
    void storeEmailValidation(EmailValidation emailValidation) throws JsonProcessingException;

    Optional<EmailValidation> getEmailValidation(String email) throws JsonMappingException, JsonProcessingException;

    void deleteKey(String key);
}
