package com.trendyTracker.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.trendyTracker.domain.Job.Tech;

public class TechUtils {
    private static List<String> techList = new ArrayList<>();

    public static List<String> getTechNameList(Set<Tech> techs) {
        for (Tech tech : techs) {
            techList.add(tech.getTech_name());
        }
        return techList;
    }
}
