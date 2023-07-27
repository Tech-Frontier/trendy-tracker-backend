package com.trendyTracker.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.trendyTracker.Dto.Recruit.RecruitDto;
import com.trendyTracker.common.Exception.ExceptionDetail.NoResultException;
import com.trendyTracker.domain.Job.Company;
import com.trendyTracker.repository.JobRepository;
import com.trendyTracker.util.UrlReader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private final JobRepository jobRepository;

     public long regisitJobPostion(String url, String companyName, String occupation) throws NoResultException, IOException {
        Set<String> techList = UrlReader.getUrlContent(url);
        
        if (techList.size() == 0)
            throw new NoResultException("해당 url 에서 tech 가 발견되지 않았습니다");

        Company company = jobRepository.registeCompany(companyName);
        return jobRepository.registJobPosition(url, company, occupation, new ArrayList<>(techList));
    }

    public RecruitDto getRecruitInfo(long recruit_id) {
        Optional<RecruitDto> result = jobRepository.getRecruit(recruit_id);
        result.orElseThrow(() -> new NotFoundException("공고 목록이 없습니다"));

        return result.get();
    }

    public List<RecruitDto> getRecruitList() throws NoResultException{
        List<RecruitDto> recruitList = jobRepository.getRecruitList();
        if (recruitList.size() ==0)
            throw new NoResultException("채용 공고가 없습니다");
        
        return recruitList;
    }
}
