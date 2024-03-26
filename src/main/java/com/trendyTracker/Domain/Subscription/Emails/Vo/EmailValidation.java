package com.trendytracker.Domain.Subscription.Emails.Vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonCreator
    public EmailValidation(
            @JsonProperty("email") String email, 
            @JsonProperty("validateCode")String validateCode,
            @JsonProperty("createTime")LocalDateTime createTime){
        this.email = email;
        this.validateCode = validateCode;
        this.createTime = createTime;
    }
}
