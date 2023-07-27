package com.trendyTracker.repository;

import java.util.List;
import java.util.Optional;

import com.trendyTracker.Dto.Recruit.RecruitDto;
import com.trendyTracker.domain.Job.Company;

public interface JobRepository {
    Company registeCompany(String company);

    long registJobPosition(String url, Company company, String jobPosition, List<String> techList);

    Optional<RecruitDto> getRecruit(long id);

    List<RecruitDto> getRecruitList();

}
