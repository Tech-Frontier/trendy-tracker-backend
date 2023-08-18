package com.trendyTracker.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.trendyTracker.domain.Job.Tech;

public class TechUtils {
    
    public static Boolean isTechNotExist(String[] techs){
    /*
     * techs 중 기술스택에 없는 것 체크 합니다.
     */
        var instance = TechListSingleton.getInstance();
        List<String> techNameList = getTechNameList(instance.getTechList());

        for (String tech : techs) {
            if(!techNameList.stream().anyMatch(t -> t.equalsIgnoreCase(tech)))
                return true;
        }
        return false;
    }

    public static List<Tech> makeTechs(String[] techs){
        List<Tech> techSet = new ArrayList<>();
        for (String tech : techs) {
            Tech newTech = new Tech(tech);
            techSet.add(newTech);
        }
        return techSet;
    }
    
    public static List<String> getTechNameList(Set<Tech> techs) {
        List<String> techList = new ArrayList<>();
        for (Tech tech : techs) {
            techList.add(tech.getTech_name());
        }
        return techList;
    }

    public static List<String> getTechNameList(List<Tech> techs) {
        List<String> techList = new ArrayList<>();
        for (Tech tech : techs) {
            techList.add(tech.getTech_name());
        }
        return techList;
    }
}
