package com.trendyTracker.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.trendyTracker.common.Exception.ExceptionDetail.AlreadyExistException;
import com.trendyTracker.domain.Job.Tech;
import com.trendyTracker.repository.TechRepository;
import com.trendyTracker.util.TechListSingleton;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TechService {
    private final TechRepository techRepository;

    public String registTechStack(String tech) throws  AlreadyExistException {
        Boolean isExist = techRepository.isTechRegist(tech);
        if (isExist)
            throw new AlreadyExistException("해당 기술 스택이 존재합니다");

        techRepository.registTechStack(tech);
        return "successfully regist";
    }

    public String deleteTechStack(String tech) {
        techRepository.deleteTechStack(tech);

        return "successfully deleted";
    }

    public List<String> getTechList() {
        List<Tech> techList = techRepository.getTechList();
        if (techList.size() ==0)
            throw new NotFoundException("개발 스택 내용이 없습니다");

        // Singleton 데이터 삽입
        TechListSingleton techListSingleton = TechListSingleton.getInstance();
        techListSingleton.setTechList(techList);

        List<String> techs = techList.stream()
                                .map(tech -> tech.getTech_name())
                                .collect(Collectors.toList());
        return techs;
    }
}
