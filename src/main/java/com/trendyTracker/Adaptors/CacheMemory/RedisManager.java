package com.trendyTracker.Adaptors.CacheMemory;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class RedisManager {
    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    private final JedisPool jedisPool;

    public RedisManager(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        this.jedisPool = new JedisPool(poolConfig, host, port);
    }

    public RedisManager(JedisPool jedisPool){
        this.jedisPool = new JedisPool();
    }

    public void storeValue(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }
    }

    public String getValue(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    public void storeList(String key, List<String> values){
        try(Jedis jedis = jedisPool.getResource()){
            jedis.del(key);
            for(String value: values){
                jedis.rpush(key, value);
            }
        }
    }

    public List<String> getLiset(String key){
        try(Jedis jedis = jedisPool.getResource()){
            return jedis.lrange(key, 0,-1);
        }
    }

}
