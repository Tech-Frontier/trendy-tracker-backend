package com.trendyTracker.Domain.RecruitTechs;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trendyTracker.Domain.Recruits.Recruit;
import com.trendyTracker.Domain.Techs.Tech;

@Repository
public interface RecruitTechRepository extends JpaRepository<RecruitTech, Long> {    
    Recruit updateJobCategory(long id, String jobCategory);

    Recruit updateRecruitTech(long id, List<Tech> techList);

    Optional<Recruit> getbyUrl(String url);

    List<Recruit> getActiveRecruits();

    long getTotalCnt();
}
