package com.trendyTracker.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.trendyTracker.domain.AppInfo;
import com.trendyTracker.repository.AppInfoRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppInfoService {

    private final AppInfoRepositoryImpl appInfoRepository;

    public AppInfo getAppInfo() {
        Optional<AppInfo> appInfos = appInfoRepository.getAppInfo();

        if (appInfos.isPresent()) {
            AppInfo appInfo = appInfos.get();
            return appInfo;
        }

        return null;
    }

    @Transactional
    public void saveAppInfo(String version, String terms) {
        appInfoRepository.saveAppInfo(version, terms);
    }

}
