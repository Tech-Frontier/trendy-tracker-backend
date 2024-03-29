package com.trendytracker.domain.job.statistic;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.trendytracker.common.exception.detail.NoResultException;
import com.trendytracker.domain.job.statistic.dto.ChartInfo;
import com.trendytracker.domain.job.statistic.dto.TrendInfo;
import com.trendytracker.domain.job.tech.TechService;
import com.trendytracker.domain.job.tech.Tech.TechType;


@SpringBootTest
public class StaticsServiceTest {
    @Autowired
    StaticsService staticsService;

    @Autowired
    TechService techService;

    @Test
    void testGetTechChartsByDatabase() {
        // given
        LocalDate fromDate = LocalDate.of(2023,1,1);
        LocalDate toDate = LocalDate.of(2024,12,30);
        TechType techType = TechType.DATABASE;
        List<String> techList = techService.getTechList(techType);

        // when
        ChartInfo techCharts = staticsService.getTechCharts(fromDate, toDate, techType);

        // then
        HashMap<String, Long> chart = techCharts.chart();
        assertTrue(techList.containsAll(chart.keySet()));
    }

    @Test
    void testGetTechChartsByLanguage() {
        // given
        LocalDate fromDate = LocalDate.of(2023,1,1);
        LocalDate toDate = LocalDate.of(2024,12,30);
        TechType techType = TechType.LANGUAGE;
        List<String> techList = techService.getTechList(techType);

        // when
        ChartInfo techCharts = staticsService.getTechCharts(fromDate, toDate, techType);

        // then
        HashMap<String, Long> chart = techCharts.chart();
        assertTrue(techList.containsAll(chart.keySet()));
    }

    @Test
    void testgetTrendsByJava() throws NoResultException{
        // given
        String techName ="Java";

        // when
        List<TrendInfo> trends = staticsService.getTrends(techName);

        // then 
        assertTrue(trends.size() >1);
    }
}
