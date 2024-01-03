package com.trendyTracker.Domain.Jobs.Statistics;

import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import com.trendyTracker.Domain.Jobs.Statistics.Dto.ChartInfo;
import com.trendyTracker.Domain.Jobs.Techs.Tech.TechType;

@Repository
public interface StaticsRepository {
    ChartInfo getTechChart(LocalDate fromDate, LocalDate toDate, TechType techType);
}
