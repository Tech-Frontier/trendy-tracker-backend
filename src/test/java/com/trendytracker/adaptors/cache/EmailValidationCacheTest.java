package com.trendytracker.adaptors.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.trendytracker.domain.subscription.email.vo.EmailValidation;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SpringBootTest
public class EmailValidationCacheTest extends RedisManager {
    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    private JedisPool jedisPool;
    private EmailValidationCacheImpl emailValidationCache;

    @BeforeEach
    public void setUp() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(poolConfig, host,port);
        emailValidationCache = new EmailValidationCacheImpl(jedisPool);
    }
    

    @Test
    public void testStoreAndGetEmailValidation() throws Exception {
        // given
        String email = "test@example.com";
        String validateCode = "12345";
        LocalDateTime createTime = LocalDateTime.now();
        EmailValidation originalValidation = new EmailValidation(email, validateCode, createTime);

        // when 
        emailValidationCache.storeEmailValidation(originalValidation);

        // then
        var retrievedValidation = emailValidationCache.getEmailValidation(email).get();
        assertNotNull(retrievedValidation);
        assertEquals(email, retrievedValidation.getEmail());
        assertEquals(validateCode, retrievedValidation.getValidateCode());
        assertEquals(createTime.truncatedTo(ChronoUnit.SECONDS), retrievedValidation.getCreateTime().truncatedTo(ChronoUnit.SECONDS));
    }
}
