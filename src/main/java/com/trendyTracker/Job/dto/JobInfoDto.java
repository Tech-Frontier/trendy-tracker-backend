package com.trendyTracker.Job.dto;

import java.util.Set;

import com.trendyTracker.Job.domain.Tech;


public record JobInfoDto (
    String title,
    Set<Tech> techSet
){
}
