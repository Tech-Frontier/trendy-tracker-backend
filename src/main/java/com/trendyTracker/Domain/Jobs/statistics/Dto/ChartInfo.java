package com.trendyTracker.Domain.Jobs.statistics.Dto;

import java.time.LocalDate;
import java.util.HashMap;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChartInfo(
    @Schema(description = "시작 날짜",example = "2023-09-01")
    LocalDate fromDate,
    @Schema(description = "완료 날짜",example = "2023-12-01")
    LocalDate toDate,
    @Schema(description = "차트 데이터",example = "HashMap")
    HashMap<String, Integer> chart
){}
