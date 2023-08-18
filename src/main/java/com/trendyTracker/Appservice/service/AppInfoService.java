package com.trendyTracker.Appservice.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.trendyTracker.Appservice.domain.AppInfo;
import com.trendyTracker.Appservice.repository.AppInfoRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppInfoService {

    private final AppInfoRepositoryImpl appInfoRepository;

    public AppInfo getAppInfo() {
        Optional<AppInfo> appInfos = appInfoRepository.getAppInfo();
        try{
            appInfos.orElseThrow(() -> new NotFoundException("dwqdqwd"));

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
