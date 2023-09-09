package com.trendyTracker.Job.repository;

import java.util.List;

import com.trendyTracker.Job.domain.Tech;
import com.trendyTracker.Job.domain.Tech.TechType;


public interface TechRepository {
    Boolean isTechRegist(String tech_name);
    
    void registTechStack(String tech_name, TechType type);

    void deleteTechStack(String tech_name);

    List<Tech> getTechList();
}
