package com.trendyTracker.util;

import java.util.ArrayList;
import java.util.List;

import com.trendyTracker.Job.domain.Tech;

public class TechListSingleton {
    // TODO 분산 시스템을 고려하면 Redis 로 변환 필요
    private static TechListSingleton instance;
    private List<Tech> techList;

    private TechListSingleton(){
        techList = new ArrayList<>();
    }

    public static TechListSingleton getInstance(){
        if(instance == null)
            instance = new TechListSingleton();

        return instance;
    }

    public void setTechList(List<Tech> techList){
        this.techList = techList;
    }

    public List<Tech> getTechList(){
        return techList;
    }
}
