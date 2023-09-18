package com.trendyTracker.Appservice.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.trendyTracker.Appservice.domain.AppInfo;
import com.trendyTracker.Appservice.repository.AppInfoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppInfoService {

    private final AppInfoRepository appInfoRepository;

    public AppInfo getAppInfo() {
        Optional<AppInfo> appInfos = appInfoRepository.getAppInfo();
        try{
            appInfos.orElseThrow(() -> new NotFoundException("not exist"));

            if (!appInfos.isPresent())
                return null;

            return appInfos.get();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return new AppInfo();
        }
    }

    @Transactional
    public void saveAppInfo(String version, String terms) {
        appInfoRepository.saveAppInfo(version, terms);
    }

}
