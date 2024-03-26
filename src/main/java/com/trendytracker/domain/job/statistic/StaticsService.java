package com.trendytracker.domain.job.statistic;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.trendytracker.domain.job.statistic.dto.ChartInfo;
import com.trendytracker.domain.job.tech.Tech;
import com.trendytracker.domain.job.tech.TechRepository;
import com.trendytracker.domain.job.tech.Tech.TechType;
import com.trendytracker.domain.job.statistic.dto.TrendInfo;
import com.trendytracker.common.exception.detail.NoResultException;

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
