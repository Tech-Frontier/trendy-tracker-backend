package com.trendytracker.Adaptors.CacheMemory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trendytracker.Adaptors.CacheMemory.TechsCacheImpl;
import com.trendytracker.Domain.Jobs.Techs.Tech;
import com.trendytracker.Domain.Jobs.Techs.TechRepository;
import com.trendytracker.Domain.Jobs.Techs.Tech.TechType;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SpringBootTest
public class TechsCacheTest {
    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;   

    @Autowired
    private TechRepository techRepository;

    private String key = "testTechsKey";
    private JedisPool jedisPool;
    private TechsCacheImpl techsCache;

    @BeforeEach
    public void setUp() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(poolConfig, host,port);
        techsCache = new TechsCacheImpl(jedisPool,techRepository,key);
    }

    @AfterEach
    public void tearDown(){
        techsCache.deleteKey(key);
    }

    @Test
    public void testStoreAndGetValue() throws JsonProcessingException {
        // given
        List<Tech> techList = new ArrayList<>();
        techList.add(new Tech("C#", TechType.LANGUAGE));
        techList.add(new Tech("Java", TechType.LANGUAGE));
        

        // when
        techsCache.storeTechList(techList);

        // then
        List<Tech> result = techsCache.getTechList();
        assertEquals(techList.get(0).getTechName(), result.get(0).getTechName());
        assertEquals(techList.get(1).getTechName(), result.get(1).getTechName());
    }
}
