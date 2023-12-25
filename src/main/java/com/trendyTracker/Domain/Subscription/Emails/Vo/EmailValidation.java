package com.trendyTracker.Domain.Subscription.Emails.Vo;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class EmailValidation {
    private final String email;
    private final String validateCode;
    private final LocalDateTime createTime;

    public EmailValidation(String email, String validateCode){
        this.email = email;
        this.validateCode = validateCode;
        this.createTime = LocalDateTime.now();
    }

    public EmailValidation(String email, String validateCode, LocalDateTime createTime){
        this.email = email;
        this.validateCode = validateCode;
        this.createTime = createTime;
    }
}
