package com.trendyTracker.Domain.Jobs.statistics;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.trendyTracker.Domain.Jobs.statistics.Dto.ChartInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaticsService {

    private final StaticsRepository staticsRepository;

    public ChartInfo getTechCharts(LocalDate fromDate, LocalDate toDate){
      return staticsRepository.getTechChart(fromDate, toDate);
    }
}
