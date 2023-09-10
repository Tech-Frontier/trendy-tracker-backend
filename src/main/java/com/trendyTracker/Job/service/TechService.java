package com.trendyTracker.Job.service;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.trendyTracker.Job.domain.Tech;
import com.trendyTracker.Job.domain.Tech.TechType;
import com.trendyTracker.Job.repository.TechRepository;
import com.trendyTracker.common.Exception.ExceptionDetail.AlreadyExistException;
import com.trendyTracker.util.TechListSingleton;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TechService {
    private final TechRepository techRepository;

    public String registTechStack(String tech, TechType type) throws  AlreadyExistException {
        Boolean isExist = techRepository.isTechRegist(tech);
        if (isExist)
            throw new AlreadyExistException("해당 기술 스택이 존재합니다");

        techRepository.registTechStack(tech, type);
        return "successfully regist";
    }

    public String deleteTechStack(String tech) {
        techRepository.deleteTechStack(tech);

        return "successfully deleted";
    }

    public JSONArray getTechList() {
        List<Tech> techList = techRepository.getTechList();
        if (techList.size() ==0)
        throw new NotFoundException("개발 스택 내용이 없습니다");
        
        // Singleton 데이터 삽입
        TechListSingleton techListSingleton = TechListSingleton.getInstance();
        techListSingleton.setTechList(techList);
        
        List<JSONObject> toBeList = new ArrayList<>();
        for (Tech tech : techList) {
            JSONObject toBeItem = new JSONObject();
            toBeItem.put(tech.getType().name(), tech.getTech_name());
            toBeList.add(0, toBeItem);
        }

        return new JSONArray(toBeList);
    }
}
