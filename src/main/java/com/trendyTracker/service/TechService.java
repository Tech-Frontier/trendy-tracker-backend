package com.trendyTracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.trendyTracker.Dto.Tech.CompanyTechStackDto;
import com.trendyTracker.Dto.Tech.TechDto;
import com.trendyTracker.repository.TechRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TechService {
    private final TechRepository techRepository;

    public String registTechStack(TechDto techDto) {
        techRepository.registTechStack(techDto);

        return "successfully regist";
    }

    public String deleteTechStack(String url) {
        Optional<TechDto> techInfo = techRepository.getTechInfo(url);
        techInfo.orElseThrow(() -> new NotFoundException("회사별 개발 스택 내용이 없습니다"));
        techRepository.deleteTechStack(url);

        return "successfully deleted";
    }

    public List<CompanyTechStackDto> getTechList() {
        Optional<List<CompanyTechStackDto>> techList = techRepository.getTechList();
        techList.orElseThrow(() -> new NotFoundException("회사별 개발 스택 내용이 없습니다"));

        return techList.get();
    }

    public TechDto getTechInfo(String url) {
        Optional<TechDto> techInfo = techRepository.getTechInfo(url);
        techInfo.orElseThrow(() -> new NotFoundException("해당 채용공고의 개발 스택 내용이 없습니다"));

        return techInfo.get();
    }

}
