package com.trendytracker.domain.job.recruittech;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitTechRepository extends JpaRepository<RecruitTech, Long> {   
}
