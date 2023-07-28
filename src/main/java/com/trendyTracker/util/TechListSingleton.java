package com.trendyTracker.util;

import java.util.ArrayList;
import java.util.List;

public class TechListSingleton {
    private static TechListSingleton instance;
    private List<String> techList;

    private TechListSingleton(){
        techList = new ArrayList<>();
    }

    public static TechListSingleton getInstance(){
        if(instance == null)
            instance = new TechListSingleton();

        return instance;
    }

    public void setTechList(List<String> techList){
        this.techList = techList;
    }

    public List<String> getTechList(){
        return techList;
    }
}
