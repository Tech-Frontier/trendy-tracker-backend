package com.trendyTracker.Domain.Techs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechRepository extends JpaRepository<Tech, String> {
    Boolean isTechRegisit(String techName);
}
