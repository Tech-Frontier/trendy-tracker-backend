package com.trendyTracker.Adaptors.CacheMemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trendyTracker.Domain.Jobs.Recruits.RecruitCache;
import com.trendyTracker.Domain.Jobs.Recruits.RecruitRepository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RecruitsCacheImpl extends RedisManager implements RecruitCache{
    @Autowired
    private RecruitRepository recruitRepository;
    private String key = "totalCnt";
    
    public RecruitsCacheImpl(){
        super();
    }

    public RecruitsCacheImpl(JedisPool jedisPool, RecruitRepository repository, String key){
        super(jedisPool);
        this.recruitRepository = repository;
        this.key = key;
    }

    @Override
    public void storeJobCnt(long totalCount){
        try(Jedis jedis = getJedisPool().getResource()){
            jedis.set(key, String.valueOf(totalCount));
        }
    }

    @Override
    public void increaseJobCnt(){
        try(Jedis jedis = getJedisPool().getResource()){
            String cnt = jedis.get(key);
            if(cnt == null)
                jedis.set(key, String.valueOf(getTotalJobCount() + 1));
            else 
                jedis.set(key, String.valueOf(Long.parseLong(cnt) + 1));
        }
    }

    @Override
    public void decreaseJobCnt(){
        try(Jedis jedis = getJedisPool().getResource()){
            String cnt = jedis.get(key);
            if(cnt == null)
                jedis.set(key, String.valueOf(getTotalJobCount() - 1));
            else 
                jedis.set(key, String.valueOf(Long.parseLong(cnt) - 1));
        }
    }

    @Override
    public Long getJobCnt(){
        try(Jedis jedis = getJedisPool().getResource()){
            String cnt = jedis.get(key);

            if(cnt == null)
                return getTotalJobCount();

            return Long.parseLong(cnt);
        }
    }

    private long getTotalJobCount(){
        return recruitRepository.count();
    }
}
