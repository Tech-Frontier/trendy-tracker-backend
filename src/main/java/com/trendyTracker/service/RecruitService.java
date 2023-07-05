package com.trendyTracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.trendyTracker.Dto.Recruit.RecruitDto;
import com.trendyTracker.repository.RecruitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;

    public List<RecruitDto> getRecruitList() {
        Optional<List<RecruitDto>> recruitList = recruitRepository.getRecruitList();
        recruitList.orElseThrow(() -> new NotFoundException("공고 목록이 없습니다"));

        return recruitList.get();
    }

    public RecruitDto getRecruitInfo(Long id) {
        Optional<RecruitDto> recruitInfo = recruitRepository.getRecruitInfo(id);
        recruitInfo.orElseThrow(() -> new NotFoundException("공고 목록이 없습니다"));

        return recruitInfo.get();
    }
}
