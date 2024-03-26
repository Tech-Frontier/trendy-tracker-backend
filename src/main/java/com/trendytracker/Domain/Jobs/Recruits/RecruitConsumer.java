package com.trendytracker.Domain.Jobs.Recruits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface RecruitConsumer<T> {
    public void registJob(T message) throws JsonMappingException, JsonProcessingException;

    public void updateJob(T message) throws JsonMappingException, JsonProcessingException;

    public void deleteJob(T message);
}
