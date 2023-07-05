package com.trendyTracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.trendyTracker.Dto.Tech.CompanyTechStackDto;
import com.trendyTracker.Dto.Tech.TechDto;

@Repository
public class TechRepositoryImpl implements TechRepository {

    @Override
    public void registTechStack(TechDto techDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registTechStack'");
    }

    @Override
    public Optional<List<CompanyTechStackDto>> getTechList() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTechList'");
    }

    @Override
    public Optional<TechDto> getTechInfo(String url) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTechInfo'");
    }

    @Override
    public void deleteTechStack(String url) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteTechStack'");
    }

}
