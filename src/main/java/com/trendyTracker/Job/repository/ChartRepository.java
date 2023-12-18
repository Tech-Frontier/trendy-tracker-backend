package com.trendyTracker.Job.repository;

import java.time.LocalDate;

import com.trendyTracker.Job.domain.Model.ChartInfo;

public interface ChartRepository {
    ChartInfo getTechChart(LocalDate fromDate, LocalDate toDate);
}
