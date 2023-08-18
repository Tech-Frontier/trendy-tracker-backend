package com.trendyTracker.repository;

import java.util.List;
import java.util.Optional;

import com.trendyTracker.Dto.Recruit.RecruitDto;
import com.trendyTracker.domain.Job.Company;
import com.trendyTracker.domain.Job.Tech;

public interface JobRepository {
    Company registeCompany(String company);

    long registJobPosition(String url, Company company, String jobPosition, List<Tech> techList);

    void deleteJobPosition(long recruit_id);
    
    Optional<RecruitDto> updateRecruitTech(long id,  List<Tech> techList);

    Optional<RecruitDto> getRecruit(long id);

    List<RecruitDto> getRecruitList(String[] companies, String[] jobCategories, String[] techs);

}
