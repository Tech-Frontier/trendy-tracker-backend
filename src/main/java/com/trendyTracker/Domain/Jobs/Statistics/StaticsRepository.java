package com.trendyTracker.Domain.Jobs.Statistics;

import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import com.trendyTracker.Domain.Jobs.Statistics.Dto.ChartInfo;

@Repository
public interface StaticsRepository {
    ChartInfo getTechChart(LocalDate fromDate, LocalDate toDate);
}
