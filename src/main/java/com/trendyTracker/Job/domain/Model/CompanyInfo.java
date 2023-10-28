package com.trendyTracker.Job.domain.Model;

import com.trendyTracker.Job.domain.Company.CompanyCategory;

import io.swagger.v3.oas.annotations.media.Schema;

public record CompanyInfo (
    @Schema(description = "회사 그룹", example = "Naver")
    String companyGroup,

    @Schema(description = "회사 규모", example = "Series_C")
    CompanyCategory companyCategory,

    @Schema(description = "회사 규모", example = "Naver_Webtoon")
    String companyName
){
}