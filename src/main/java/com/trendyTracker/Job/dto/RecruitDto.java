package com.trendyTracker.Job.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.trendyTracker.Job.domain.Company;

import io.swagger.v3.oas.annotations.media.Schema;

public record RecruitDto(
        long id,
        @Schema(description = "회사명", example = "네이버") String company,
        @Schema(description = "직군", example = "백엔드") String occupation,
        @Schema(description = "채용 페이지", example = "https://naver.com") String url,
        @Schema(description = "제목", example = "2023년 하반기 백엔드 직군") String title,
        @Schema(description = "생성 시간", example = "YYYY-MM-DD HH24:mi:ss")LocalDateTime createdTime,
        @Schema(description = "테크 항목", example = "[C#, Java]") List<String> techList
) {
    public RecruitDto(long id, Company company, String occupation, String url, String title, LocalDateTime createTime) {
        this(id, company.getCompany_name(), occupation, url, title, createTime, null);
    }

    public RecruitDto(long id, Company company, String occupation, String url, String title, LocalDateTime createTime, List<String> techList) {
        this(id, company.getCompany_name(), occupation, url, title, createTime, techList);
    }
}
