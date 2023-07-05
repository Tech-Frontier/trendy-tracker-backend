package com.trendyTracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.trendyTracker.Dto.Recruit.RecruitDto;

@Repository
public class RecruitRepositoryImpl implements RecruitRepository {

    @Override
    public Optional<List<RecruitDto>> getRecruitList() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRecruitList'");
    }

    @Override
    public Optional<RecruitDto> getRecruitInfo(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRecruitInfo'");
    }

}
