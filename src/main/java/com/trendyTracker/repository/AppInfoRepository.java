package com.trendyTracker.repository;

import java.util.Optional;

import com.trendyTracker.domain.AppInfo;

public interface AppInfoRepository {
    Optional<AppInfo> getAppInfo();

    void saveAppInfo(String version, String terms);
}
