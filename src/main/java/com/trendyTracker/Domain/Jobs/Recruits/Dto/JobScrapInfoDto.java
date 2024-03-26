package com.trendytracker.Domain.Jobs.Recruits.Dto;

import java.util.Set;

import com.trendytracker.Domain.Jobs.Techs.Tech;

import io.swagger.v3.oas.annotations.media.Schema;

public record JobScrapInfoDto (
    @Schema(description = "제목", example = "오늘 하루 수고했어") String title,
    @Schema(description = "기술 Set", example = "Set<Tech>") Set<Tech> techSet
){
}
