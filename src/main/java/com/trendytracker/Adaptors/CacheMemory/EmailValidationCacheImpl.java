package com.trendytracker.Adaptors.CacheMemory;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.trendytracker.Domain.Subscription.Emails.EmailValidationCache;
import com.trendytracker.Domain.Subscription.Emails.Vo.EmailValidation;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class EmailValidationCacheImpl extends RedisManager implements EmailValidationCache{

    public EmailValidationCacheImpl(){
        super();
    }

    public EmailValidationCacheImpl(JedisPool jedisPool){
        super(jedisPool);
    }

    @Override
    public void storeEmailValidation(EmailValidation emailValidation) throws JsonProcessingException {
        String json = getMapper().writeValueAsString(emailValidation);

        try (Jedis jedis = getJedisPool().getResource()) {
            jedis.set(emailValidation.getEmail(), json);
        }
    }

    @Override
    public Optional<EmailValidation> getEmailValidation(String email) throws JsonMappingException, JsonProcessingException  {
         try (Jedis jedis = getJedisPool().getResource()) {
            String json = jedis.get(email);
            if (json == null) {
                return Optional.empty();
            }
            return Optional.of(getMapper().readValue(json, EmailValidation.class));
        }
    }
}
