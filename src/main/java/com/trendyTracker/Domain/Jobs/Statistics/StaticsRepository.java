package com.trendytracker.Domain.Jobs.Statistics;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.trendytracker.Domain.Jobs.Statistics.Dto.ChartInfo;
import com.trendytracker.Domain.Jobs.Statistics.Dto.TrendInfo;
import com.trendytracker.Domain.Jobs.Techs.Tech;
import com.trendytracker.Domain.Jobs.Techs.Tech.TechType;

@Repository
public interface StaticsRepository {
    ChartInfo getTechChart(LocalDate fromDate, LocalDate toDate, TechType techType);

    List<TrendInfo> getTrendInfo(Tech tech);
}
