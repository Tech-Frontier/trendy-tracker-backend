package com.trendyTracker.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.trendyTracker.Job.domain.Tech;

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
    /*
     * techs 를 List<Tech> 로 변환 합니다.
     */
        var instance = TechListSingleton.getInstance();
        List<Tech> techNameList = instance.getTechList();

        return techNameList.stream()
            .filter(techName -> Arrays.stream(techs)
            .anyMatch(tech -> techName.getTech_name().equalsIgnoreCase(tech)))
            .map(techName -> new Tech(techName.getTech_name(), techName.getType()))
            .collect(Collectors.toList());
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
