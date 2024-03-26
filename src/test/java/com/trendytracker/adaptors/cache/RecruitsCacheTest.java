package com.trendytracker.adaptors.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.trendytracker.domain.job.recruit.RecruitRepository;

import jakarta.transaction.Transactional;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SpringBootTest
@Transactional
public class RecruitsCacheTest {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Autowired
    private RecruitRepository recruitRepository;

    private String key = "testRecruitKey";
    private JedisPool jedisPool;
    private RecruitsCacheImpl recruitsCache;

    @BeforeEach
    public void setUp() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(poolConfig, host,port);
        recruitsCache = new RecruitsCacheImpl(jedisPool,recruitRepository, key);
    }

    @AfterEach
    public void tearDown(){
        recruitsCache.deleteKey(key);
    }

    @Test
    public void testStoreJobCnt(){
        // given
        long totalCnt = 10L;

        // when
        recruitsCache.storeJobCnt(totalCnt);

        // then
        Long jobCnt = recruitsCache.getJobCnt();
        assertEquals(jobCnt, totalCnt);
    }

    @Test
    public void testIncreaseJobCnt() {
        // given
        long totalCnt = 10L;
        recruitsCache.storeJobCnt(totalCnt);

        // when 
        recruitsCache.increaseJobCnt();

        // then 
        Long jobCnt = recruitsCache.getJobCnt();
        assertEquals(jobCnt, 11L);
    }

    @Test
    public void testDecreaseJobCnt() {
        // given
        long totalCnt = 10L;
        recruitsCache.storeJobCnt(totalCnt);

        // when 
        recruitsCache.decreaseJobCnt();

        // then 
        Long jobCnt = recruitsCache.getJobCnt();
        assertEquals(jobCnt, 9L);
    }

    @Test
    public void testGetJobCntWithNoKey() {
        // given
        recruitsCache.deleteKey(key);

        // when 
        Long jobCnt = recruitsCache.getJobCnt();

        // then 
        assertTrue(jobCnt > 0);
    }

}
