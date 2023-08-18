package com.trendyTracker.Job.dto;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyTechStackDto {
    @Schema(description = "회사명", example = "네이버")
    private String company;

    @Schema(description = "회사별 개발 스택", example = "[C#, .Net, WPF]")
    private List<String> stackByCompany;

    @Schema(description = "공고별 개발 스택", example = "{'https://naver.com' , [C#, .Net, WPF]")
    private Map<String, List<String>> stackByUrl;
}
