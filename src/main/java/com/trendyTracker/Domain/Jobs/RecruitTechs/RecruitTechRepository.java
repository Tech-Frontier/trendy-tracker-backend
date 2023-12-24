package com.trendyTracker.Domain.Jobs.RecruitTechs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitTechRepository extends JpaRepository<RecruitTech, Long> {   
}
