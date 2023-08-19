package com.trendyTracker.Job.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TechDto(
        @NotNull
        @Schema(description = "채용 페이지", example = "https://naver.com") String url,
        @Schema(description = "개발 스택", example = "[C#, .Net, WPF]") List<String> techStack
) {
}
