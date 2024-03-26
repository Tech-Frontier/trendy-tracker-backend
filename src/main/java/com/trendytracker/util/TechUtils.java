package com.trendytracker.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.trendytracker.adaptors.cache.TechsCacheImpl;
import com.trendytracker.domain.job.tech.Tech;


public class TechUtils {
    
    public static Boolean isTechNotExist(String[] techs, TechsCacheImpl techsCache) throws JsonMappingException, JsonProcessingException{
    /*
     * techs 중 기술스택에 없는 것 체크 합니다.
     */
        List<Tech> techList = techsCache.getTechList();

        for (String tech : techs) {
            if(!techList.stream().anyMatch(t -> t.getTechName().equalsIgnoreCase(tech)))
                return true;
        }
        return false;
    }

    public static List<Tech> makeTechs(String[] techs, TechsCacheImpl techsCache) throws JsonMappingException, JsonProcessingException{
    /*
     * techs 를 List<Tech> 로 변환 합니다.
     */
        List<Tech> techList = techsCache.getTechList();

        return techList.stream()
            .filter(techName -> Arrays.stream(techs)
            .anyMatch(tech -> techName.getTechName().equalsIgnoreCase(tech)))
            .map(techName -> new Tech(techName.getTechName(), techName.getType()))
            .collect(Collectors.toList());
    }

    public static List<String> getTechNameList(Set<Tech> techs) {
        List<String> techList = new ArrayList<>();
        for (Tech tech : techs) {
            techList.add(tech.getTechName());
        }
        return techList;
    }

    public static List<String> getTechNameList(List<Tech> techs) {
        List<String> techList = new ArrayList<>();
        for (Tech tech : techs) {
            techList.add(tech.getTechName());
        }
        return techList;
    }
}
