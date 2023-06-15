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

<<<<<<< HEAD
<<<<<<< HEAD
        if (!appInfos.isPresent())
            return null;

        return appInfos.get();
=======
        if (appInfos.isPresent()) {
            AppInfo appInfo = appInfos.get();
            return appInfo;
        }

        return null;
>>>>>>> 32d3f22 (app_info 테이블 생성 및 뼈대 설정)
=======
        if (!appInfos.isPresent())
            return null;

        return appInfos.get();
>>>>>>> 433f679 (feed back 반영)
    }

    @Transactional
    public void saveAppInfo(String version, String terms) {
        appInfoRepository.saveAppInfo(version, terms);
    }

}
