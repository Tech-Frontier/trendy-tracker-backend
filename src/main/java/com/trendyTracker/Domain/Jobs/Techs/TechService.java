package com.trendyTracker.Domain.Jobs.Techs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.NotFoundException;
import org.springframework.stereotype.Service;

import com.trendyTracker.Common.Exception.ExceptionDetail.AlreadyExistException;
import com.trendyTracker.Domain.Jobs.Techs.Tech.TechType;
import com.trendyTracker.Util.TechListSingleton;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TechService {
    
    private final TechRepository techRepository;

    @Transactional
    public Tech registTechStack(String tech, TechType type) throws AlreadyExistException{
        if(techRepository.findById(tech).isPresent())
            throw new AlreadyExistException("해당 기술 스택이 존재합니다");
        
        return techRepository.save(new Tech(tech, type));
    }

    @Transactional
    public String deleteTechStack(String tech){
        Tech findTech = techRepository.findById(tech)
            .orElseThrow(() -> new NotFoundException("개발 스택 내용이 없습니다"));

        techRepository.delete(findTech);

        return "successfully deleted";
    }

    public List<Map<String,String>> getTechList(){
        List<Tech> techList = techRepository.findAll();
        if(techList.isEmpty())
            return new ArrayList<>();

        TechListSingleton instance = TechListSingleton.getInstance();
        instance.setTechList(techList);

        return techList.stream()
                .map(tech -> Map.of(
                    "name", tech.getTechName(),
                    "type", tech.getType().name()
                ))
                .collect(Collectors.toList());
    }
}
