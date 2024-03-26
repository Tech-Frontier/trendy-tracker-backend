package com.trendytracker.api.recruit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.trendytracker.domain.job.tech.Tech.TechType;



public class StaticsControllerTest {
    @Test
    void testGetChartInfo() {
        // given
        TechType techType = TechType.DATABASE;

        // when
        String name = techType.name();

        // then 
        assertEquals("DATABASE", name);
    }
}
