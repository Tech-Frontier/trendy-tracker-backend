package com.trendyTracker.Job.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.trendyTracker.Job.domain.Model.ChartInfo;
import com.trendyTracker.Job.repository.ChartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChartService {
    
    private final ChartRepository chartRepository;

    public ChartInfo getTechCharts(LocalDate fromDate, LocalDate toDate){
        ChartInfo techChart = chartRepository.getTechChart(fromDate, toDate);
        return techChart;
    }
}
