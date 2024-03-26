package com.trendytracker.Domain.Jobs.Recruits;

import org.springframework.stereotype.Repository;

@Repository
public interface RecruitCache {
    void storeJobCnt(long totalCnt);

    void increaseJobCnt();

    void decreaseJobCnt();
    
    Long getJobCnt();    

    void deleteKey(String key);
}
