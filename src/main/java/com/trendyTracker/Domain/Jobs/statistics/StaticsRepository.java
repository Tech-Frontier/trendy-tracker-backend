package com.trendyTracker.Domain.Jobs.statistics;

import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import com.trendyTracker.Domain.Jobs.statistics.Dto.ChartInfo;

@Repository
public interface StaticsRepository {
    ChartInfo getTechChart(LocalDate fromDate, LocalDate toDate);
}
