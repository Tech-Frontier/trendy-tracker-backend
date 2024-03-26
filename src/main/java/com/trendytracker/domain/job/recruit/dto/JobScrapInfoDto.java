package com.trendytracker.domain.job.recruit.dto;

import java.util.Set;

import com.trendytracker.domain.job.tech.Tech;

import io.swagger.v3.oas.annotations.media.Schema;

public record JobScrapInfoDto (
    @Schema(description = "제목", example = "오늘 하루 수고했어") String title,
    @Schema(description = "기술 Set", example = "Set<Tech>") Set<Tech> techSet
){
}
