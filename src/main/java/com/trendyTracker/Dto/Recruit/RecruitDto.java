package com.trendyTracker.Dto.Recruit;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruitDto {
    private long id;

    @Schema(description = "회사명", example = "네이버")
    private String company;

    @Schema(description = "직군", example = "백엔드")
    private String occupation;

    @Schema(description = "채용 페이지", example = "https://naver.com")
    private String url;

    @Schema(description = "생성 시간", example = "YYYY-MM-DD HH24:mi:ss")
    private LocalDateTime createdTime;
}
