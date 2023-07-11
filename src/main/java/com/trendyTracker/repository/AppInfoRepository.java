package com.trendyTracker.repository;

import java.util.Optional;

import com.trendyTracker.domain.AppService.AppInfo;

public interface AppInfoRepository {
    Optional<AppInfo> getAppInfo();

    void saveAppInfo(String version, String terms);
}
