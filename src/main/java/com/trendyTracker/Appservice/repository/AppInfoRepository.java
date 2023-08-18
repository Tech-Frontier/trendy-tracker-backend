package com.trendyTracker.Appservice.repository;

import java.util.Optional;

import com.trendyTracker.Appservice.domain.AppInfo;

public interface AppInfoRepository {
    Optional<AppInfo> getAppInfo();

    void saveAppInfo(String version, String terms);
}
