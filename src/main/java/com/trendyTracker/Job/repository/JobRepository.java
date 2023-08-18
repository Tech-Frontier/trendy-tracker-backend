package com.trendyTracker.Job.repository;

import java.util.List;
import java.util.Optional;

import com.trendyTracker.Job.domain.Company;
import com.trendyTracker.Job.domain.Tech;
import com.trendyTracker.Job.dto.RecruitDto;

public interface JobRepository {
    Company registeCompany(String company);

    long registJobPosition(String url, Company company, String jobPosition, List<Tech> techList);

    void deleteJobPosition(long recruit_id);
    
    Optional<RecruitDto> updateRecruitTech(long id,  List<Tech> techList);

    Optional<RecruitDto> getRecruit(long id);

    List<RecruitDto> getRecruitList(String[] companies, String[] jobCategories, String[] techs);

}
