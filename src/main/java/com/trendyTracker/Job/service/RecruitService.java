package com.trendyTracker.Job.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.trendyTracker.Job.domain.Company;
import com.trendyTracker.Job.domain.Tech;
import com.trendyTracker.Job.dto.RecruitDto;
import com.trendyTracker.Job.repository.JobRepository;
import com.trendyTracker.common.Exception.ExceptionDetail.NoResultException;
import com.trendyTracker.common.Exception.ExceptionDetail.NotAllowedValueException;
import com.trendyTracker.util.JobTotalCntSingleton;
import com.trendyTracker.util.TechUtils;
import com.trendyTracker.util.UrlReader;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private final JobRepository jobRepository;
    JobTotalCntSingleton jobTotalCntSingleton = JobTotalCntSingleton.getInstance();

     public long regisitJobPostion(String url, String companyName, String jobCategory) throws NoResultException, IOException {
    /*
     * url, company, jobCategory 를 활용해 채용공고 등록합니다
     */
        Set<Tech> techList = UrlReader.getUrlContent(url);
        if (techList.size() == 0)
            throw new NoResultException("해당 url 에서 tech 가 발견되지 않았습니다");

        Company newCompany = jobRepository.registeCompany(companyName.toLowerCase());
        // Singleton 데이터 변경
        jobTotalCntSingleton.increaseCnt();

        return jobRepository.registJobPosition(url, newCompany, jobCategory.toLowerCase(), new ArrayList<>(techList));
    }

    public RecruitDto getRecruitInfo(long recruit_id) {
    /*
     * 채용공고 조회합니다.
     */
        Optional<RecruitDto> result = jobRepository.getRecruit(recruit_id);
        result.orElseThrow(() -> new NotFoundException("공고 목록이 없습니다"));

        return result.get();
    }

    public void deleteRecruit(long recruit_id){
    /*
     * 채용공고 비활성화 합니다.
     */
        var recruitInfo = getRecruitInfo(recruit_id);
        // Singleton 데이터 변경
        jobTotalCntSingleton.decreaseCnt();

        jobRepository.deleteJobPosition(recruitInfo.id());
    }

    public RecruitDto updateRecruitTechs(long recruit_id, String[] techs) throws NotAllowedValueException{
    /*
     * 채용 공고의 기술 스택을 변경합니다.
     */
        var recruitInfo = getRecruitInfo(recruit_id);
        if(TechUtils.isTechNotExist(techs))
            throw new NotAllowedValueException("존재하지 않는 기술이 존재합니다");

        List<Tech> techList = TechUtils.makeTechs(techs);
        Optional<RecruitDto> result = jobRepository.updateRecruitTech(recruitInfo.id(),techList);
        return result.get();
    }


    public List<RecruitDto> getRecruitList(String[] companies, String[] jobCategories, String[] techs,
                                            Integer pageNo, Integer pageSize) throws NoResultException, ValidationException{
    /*
     * 채용 공고를 필터별로 조회합니다
     */
        // 페이징 파리미터 처리 
        if ((pageNo == null && pageSize != null) || (pageNo != null && pageSize == null))
            throw new ValidationException("pageNo, pageSize Error");
        // 필수 파라미터 처리 
        if (companies[0].equals("*")) 
            companies = null;
        // techs 소문자 처리
        companies = changeToLowerCase(companies);
        jobCategories = changeToLowerCase(jobCategories);
        techs = changeToLowerCase(techs);

        List<RecruitDto> recruitList = jobRepository.getRecruitList(companies, jobCategories, techs);
        if (recruitList.size() ==0)
            throw new NoResultException("채용 공고가 없습니다");
        
        return pagingProcess(pageNo, pageSize, recruitList);
    }

    private String[] changeToLowerCase(String[] paramStrings){
    /*
     * Input params 를 소문자로 변경
     */
        if (paramStrings != null && paramStrings.length > 0) 
           return  Arrays.stream(paramStrings).map(param -> param.toLowerCase()).toArray(String[]::new);
        
        return paramStrings;
    }

    private List<RecruitDto> pagingProcess(Integer pageNo, Integer pageSize, List<RecruitDto> recruitList) {
    /*
    *  pageNo, pageSize 를 통해서 페이징 처리를 합니다.
    */
        if (pageNo != null){
            int startIndex = (pageNo - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, recruitList.size());

            if(endIndex < startIndex)
                throw new ValidationException("pageNo is not exist");

            return recruitList.subList(startIndex, endIndex);    
        }
        return recruitList;
    }

    public long getTotalJobCnt(){
        long totalJobCnt = jobRepository.getTotalJobCnt();

        // Singleton 데이터 삽입
        jobTotalCntSingleton.setTotalCnt(totalJobCnt);

        return totalJobCnt;
    }
}
