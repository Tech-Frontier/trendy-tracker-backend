package com.trendyTracker.repository;

import java.util.List;
import java.util.Optional;

import com.trendyTracker.Dto.Tech.CompanyTechStackDto;
import com.trendyTracker.Dto.Tech.TechDto;

public interface TechRepository {
    void registTechStack(TechDto techDto);

    void deleteTechStack(String url);

    Optional<List<CompanyTechStackDto>> getTechList();

    Optional<TechDto> getTechInfo(String url);
}
