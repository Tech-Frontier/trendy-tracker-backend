package com.trendyTracker.Domain.Jobs.Techs;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Repository
public interface TechsCache {
    void storeTechList(List<Tech> techList) throws JsonProcessingException;

    List<Tech> getTechList() throws JsonMappingException, JsonProcessingException;

    void deleteKey(String key);
}
