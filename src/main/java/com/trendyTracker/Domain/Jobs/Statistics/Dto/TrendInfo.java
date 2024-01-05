package com.trendyTracker.Domain.Jobs.Statistics.Dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;


public record TrendInfo (
    @Schema(description = "Reference date",example = "2023-09")
    LocalDate date,
    @Schema(description = "Number of cases", example = "2")
    long count,
    @Schema(description = "percent", example = "20%")
    String percent
){
}