package com.trendyTracker.repository;

import java.util.List;
import java.util.Optional;

public interface TechRepository {
    Boolean isTechRegist(String tech_name);
    
    void registTechStack(String tech_name);

    void deleteTechStack(String tech_name);

    Optional<List<String>> getTechList();
}
