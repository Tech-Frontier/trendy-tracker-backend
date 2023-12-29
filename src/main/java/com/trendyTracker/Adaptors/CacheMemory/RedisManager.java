package com.trendyTracker.Adaptors.CacheMemory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PostConstruct;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public abstract class RedisManager {
    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;
    
    private ObjectMapper mapper = new ObjectMapper();
    private JedisPool jedisPool;

    public RedisManager(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        this.jedisPool = new JedisPool(poolConfig, host, port);
        this.mapper.registerModule(new JavaTimeModule());
    }

    public RedisManager(JedisPool jedisPool){
        this.jedisPool = jedisPool;
        this.mapper.registerModule(new JavaTimeModule());
    }

    @PostConstruct
    public void init() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        this.jedisPool = new JedisPool(poolConfig, host, port);
        this.mapper.registerModule(new JavaTimeModule());
    }

    protected ObjectMapper getMapper(){
        return this.mapper;
    }

    protected JedisPool getJedisPool(){
        return this.jedisPool;
    }
    
    public void deleteKey(String key){
        try(Jedis jedis = jedisPool.getResource()){
            jedis.del(key);
        }
    }
}
