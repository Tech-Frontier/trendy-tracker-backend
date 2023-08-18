package com.trendyTracker.util;

import java.util.ArrayList;
import java.util.List;

import com.trendyTracker.Job.domain.Tech;

public class TechListSingleton {
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
