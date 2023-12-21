package com.trendyTracker.Domain.Jobs;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.trendyTracker.Domain.Recruits.Recruit;


@Repository
public interface JobRepository {
    List<Recruit> getJobs(String[] companies, String[] jobCategories, String[] techNames, Integer pageNo, Integer pageSize);

    
}
