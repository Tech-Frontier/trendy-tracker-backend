package com.trendyTracker.Job.dto;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

public record CompanyTechStackDto(
        @Schema(description = "회사명", example = "네이버") String company,
        @Schema(description = "회사별 개발 스택", example = "[C#, .Net, WPF]") List<String> stackByCompany,
        @Schema(description = "공고별 개발 스택", example = "{'https://naver.com', [C#, .Net, WPF]") Map<String, List<String>> stackByUrl
) {
}
