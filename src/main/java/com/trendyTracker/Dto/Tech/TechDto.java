package com.trendyTracker.Dto.Tech;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechDto {
    @NotNull
    @Schema(description = "채용 페이지", example = "https://naver.com")
    private String url;

    @Schema(description = "개발 스택", example = "[C#, .Net, WPF]")
    private List<String> techStack;
}
