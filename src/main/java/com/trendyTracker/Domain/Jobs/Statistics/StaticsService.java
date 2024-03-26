package com.trendytracker.Domain.Jobs.Statistics;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.trendytracker.Common.Exception.ExceptionDetail.NoResultException;
import com.trendytracker.Domain.Jobs.Statistics.Dto.ChartInfo;
import com.trendytracker.Domain.Jobs.Statistics.Dto.TrendInfo;
import com.trendytracker.Domain.Jobs.Techs.Tech;
import com.trendytracker.Domain.Jobs.Techs.TechRepository;
import com.trendytracker.Domain.Jobs.Techs.Tech.TechType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaticsService {
    private final TechRepository techRepository;
    private final StaticsRepository staticsRepository;

    public ChartInfo getTechCharts(LocalDate fromDate, LocalDate toDate, TechType techType){
      return staticsRepository.getTechChart(fromDate, toDate, techType);
    }

    
    public List<TrendInfo> getTrends(String techName) throws NoResultException {
        Tech tech = techRepository.findById(techName)
              .orElseThrow(() -> new NoResultException(techName + " 해당 기술 스택이 존재하지 않습니다"));

        return staticsRepository.getTrendInfo(tech);
    }
}
