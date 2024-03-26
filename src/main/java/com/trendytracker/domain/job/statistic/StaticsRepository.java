package com.trendytracker.domain.job.statistic;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.trendytracker.domain.job.statistic.dto.ChartInfo;
import com.trendytracker.domain.job.statistic.dto.TrendInfo;
import com.trendytracker.domain.job.tech.Tech.TechType;
import com.trendytracker.domain.job.tech.Tech;

@Repository
public interface StaticsRepository {
    ChartInfo getTechChart(LocalDate fromDate, LocalDate toDate, TechType techType);

    List<TrendInfo> getTrendInfo(Tech tech);
}
