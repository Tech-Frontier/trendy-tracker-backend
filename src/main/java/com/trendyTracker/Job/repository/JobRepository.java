package com.trendyTracker.Job.repository;

import java.util.List;
import java.util.Optional;

import com.trendyTracker.Job.domain.Company;
import com.trendyTracker.Job.domain.Recruit;
import com.trendyTracker.Job.domain.Tech;

public interface JobRepository {
    Recruit registJobPosition(String url, String title, Company company, String jobPosition, List<Tech> techList);

    Recruit updateJobPosition(long id, List<Tech> techList);

    void deleteJobPosition(Recruit recruit_id);
    
    Optional<Recruit> updateRecruitTech(long id,  List<Tech> techList);

    Optional<Recruit> getRecruit(long id);

    Optional<Recruit> getRecruitByUrl(String url);

    List<Recruit> getRecruitList(String[] companies, String[] jobCategories, String[] techs);

    long getTotalJobCnt();

}
