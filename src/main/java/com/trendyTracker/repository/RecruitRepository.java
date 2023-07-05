package com.trendyTracker.repository;

import java.util.List;
import java.util.Optional;

import com.trendyTracker.Dto.Recruit.RecruitDto;

public interface RecruitRepository {
    Optional<List<RecruitDto>> getRecruitList();

    Optional<RecruitDto> getRecruitInfo(Long id);
}
