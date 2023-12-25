package com.trendyTracker.Domain.Jobs.Recruits;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface RecruitRepositoryCustom {
    Recruit updateRecuitCategory(long id, String jobCategory);

    Optional<Recruit> findByUrl(String url);

    List<Recruit> getActiveRecruits();

    List<Recruit> getRecruitList(String[] companies, String[] jobCategories, String[] techs, Integer pageNo, Integer pageSize);
}
