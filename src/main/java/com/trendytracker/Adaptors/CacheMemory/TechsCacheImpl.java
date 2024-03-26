package com.trendytracker.Adaptors.CacheMemory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.trendytracker.Domain.Jobs.Techs.Tech;
import com.trendytracker.Domain.Jobs.Techs.TechRepository;
import com.trendytracker.Domain.Jobs.Techs.TechsCache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

@Component
public class TechsCacheImpl extends RedisManager implements TechsCache{    
    @Autowired
    private TechRepository techRepository;
    private String key = "techList";
    
    public TechsCacheImpl(){
        super();
    }

    public TechsCacheImpl(JedisPool jedisPool, TechRepository techRepository, String key){
        super(jedisPool);
        this.techRepository =techRepository;
        this.key = key;
    }

    @Override
    public void storeTechList(List<Tech> techList) throws JsonProcessingException {
        try (Jedis jedis = getJedisPool().getResource()) {
            Pipeline pipeline = jedis.pipelined();
            pipeline.del(key);

            for (Tech tech : techList) {
                String json = getMapper().writeValueAsString(tech);
                pipeline.rpush(key, json);
            }
            pipeline.sync();
        }
    }

    @Override
    public List<Tech> getTechList() throws JsonMappingException, JsonProcessingException{
        List<Tech> results = new ArrayList<>();

        try(Jedis jedis = getJedisPool().getResource()){
            List<String> rawData = jedis.lrange(key, 0, -1);

            if(rawData.size() == 0)
                return getTechStacks();

            for(String raw : rawData){
                Tech value = getMapper().readValue(raw, Tech.class);
                results.add(value);
            }
            return results;
        }
    }

    private List<Tech> getTechStacks(){
        return techRepository.findAll();
    }
}
