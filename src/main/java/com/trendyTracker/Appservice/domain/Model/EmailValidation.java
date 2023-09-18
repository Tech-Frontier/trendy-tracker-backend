package com.trendyTracker.Appservice.domain.Model;

import java.time.LocalDateTime;

public record EmailValidation(
    String email,
    String validateCode,
    LocalDateTime createTime) {

    public EmailValidation(String email, String validateCode) {
        this(email, validateCode, LocalDateTime.now());
    }

    public EmailValidation(String email, String validateCode, LocalDateTime createTime) {
        this.email = email;
        this.validateCode = validateCode;
        this.createTime = createTime;
    }
}
