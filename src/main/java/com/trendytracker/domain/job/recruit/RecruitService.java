package com.trendytracker.domain.job.recruit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.openqa.selenium.NotFoundException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.trendytracker.adaptors.cache.RecruitsCacheImpl;
import com.trendytracker.adaptors.cache.TechsCacheImpl;
import com.trendytracker.common.exception.detail.NoResultException;
import com.trendytracker.common.exception.detail.NotAllowedValueException;
import com.trendytracker.domain.job.company.Company;
import com.trendytracker.domain.job.company.CompanyRepository;
import com.trendytracker.domain.job.company.vo.CompanyInfo;
import com.trendytracker.domain.job.recruit.dto.JobScrapInfoDto;
import com.trendytracker.domain.job.recruittech.RecruitTech;
import com.trendytracker.domain.job.recruittech.RecruitTechRepository;
import com.trendytracker.domain.job.tech.Tech;
import com.trendytracker.util.TechUtils;
import com.trendytracker.util.UrlReader;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private final CompanyRepository companyRepository;
    private final RecruitRepository recruitRepository;
    private final RecruitTechRepository recruitTechRepository;
    private final RecruitsCacheImpl recruitsCache;
    private final TechsCacheImpl techsCache;


    public Recruit getRecruit(long id){
    /*
     * 채용공고 조회 by id 
     */
        Recruit recruit = recruitRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("공고 목록이 없습니다"));

        return recruit;
    }

    public Recruit getRecruit(String url){
    /*
     * 채용공고 조회 by url
     */
        Recruit recruit = recruitRepository.findByUrl(url)
            .orElseThrow(() -> new NotFoundException("공고 목록이 없습니다"));

        return recruit;
    }

    public Boolean isExsit(String url){
        Optional<Recruit> recruit = recruitRepository.findByUrl(url);
        if(recruit.isPresent())
            return true;
            
        return false;
    }

    public long getTotalCnt(){
        return recruitRepository.count();
    }

    @Transactional
    public Recruit registRecruit(String url, String company, String jobCategory) throws IOException, NoResultException{
    /*
     * url , company, jobCategory 를 활용해 채용공고 등록
     */
        JobScrapInfoDto urlContent = UrlReader.getUrlContent(url,techsCache);

        if(urlContent.techSet().isEmpty())
            throw new NoResultException("해당 url 에서 tech 가 발견되지 않았습니다");
        
        Company findCompany = companyRepository.findByCompanyName(company)
            .orElseGet(() -> {
                CompanyInfo companyInfo = new CompanyInfo(null, null, company);
                return companyRepository.save(new Company(companyInfo));
            });
        
        recruitsCache.increaseJobCnt();
        
        Recruit recruit = recruitRepository.save(
            new Recruit(url, urlContent.title(), findCompany, jobCategory));

        saveRecruitTechs(urlContent, recruit);
        return recruit;

    }

    @Transactional
    public void deleteRecruit(long id){
    /*
     * 채용공고 비활성화
     */
        Recruit recruit = getRecruit(id);
        recruit.deleteRecruit();

        recruitRepository.delete(recruit);
        recruitsCache.decreaseJobCnt();
    }

    @Transactional
    public Recruit updaRecruitTechs(long id, String[] techs) throws NotAllowedValueException, JsonMappingException, JsonProcessingException{
    /*
     * 채용공고 기술 스택 변경
     */
        Recruit recruit = getRecruit(id);
        if(TechUtils.isTechNotExist(techs,techsCache))
            throw new NotAllowedValueException("존재하지 않는 기술이 존재합니다");

        List<Tech> techList = TechUtils.makeTechs(techs, techsCache);

        updateRecruitTechs(techList,recruit);        
        return recruit;
    }

    @Transactional
    public Recruit updateRecruitInfo(String url) throws IOException, NoResultException{
    /*
     * 기존 채용공고 새로 Scrapping
     */
        Recruit recruit = getRecruit(url);
        JobScrapInfoDto urlContent = UrlReader.getUrlContent(url, techsCache);

        if(urlContent.techSet().isEmpty())
            throw new NoResultException("해당 url 에서 tech 가 발견되지 않았습니다");
        
        var techList = new ArrayList<>(urlContent.techSet());            
        updateRecruitTechs(techList, recruit);        
        return recruit;
    }

    public List<Recruit> getRecuitList(
        String[] companies,
        String[] jobCategories,
        String[] techs,
        Integer pageNo,
        Integer pageSize
    ){
    /*
     * 채용공고 필터별 조회
     */
        // 페이징 파라미터 처리
        if((pageNo == null && pageSize != null) || (pageNo != null && pageSize == null))
            throw new ValidateException("pageNo, pageSize Error");
        
        // 필수 파라미터 처리
        if(companies[0].equals("*")) companies = null;

        // 소문자 처리 
        companies = changeToLowerCase(companies);
        jobCategories = changeToLowerCase(jobCategories);
        techs = changeToLowerCase(techs);

        List<Recruit> recruitList = recruitRepository.getRecruitList(
            companies, jobCategories, techs, pageNo, pageSize);

        if(recruitList.isEmpty()) return new ArrayList<>();

        return recruitList;
    }

    private String[] changeToLowerCase(String[] paramStrings) {
    /*
     * Input params 를 소문자로 변경
     */
        if (paramStrings != null && paramStrings.length > 0) 
            return Arrays.stream(paramStrings)
                .map(param -> param.toLowerCase())
                .toArray(String[]::new);

        return paramStrings;
    }

    @Transactional
    private void saveRecruitTechs(JobScrapInfoDto urlContent, Recruit recruit) {
    /*
     * 채용기술 저장
     */
        List<Tech> techList = new ArrayList<>(urlContent.techSet());
        for (Tech tech : techList) {
            RecruitTech recruitTech = new RecruitTech(recruit, tech);
            recruitTechRepository.save(recruitTech);
        }
    }

    @Transactional
    private void updateRecruitTechs(List<Tech> techList, Recruit recruit) {
    /*
     * 채용기술 변경
     */
        var recruitTechs = recruit.getRecruitTechs();        
        for(RecruitTech recruitTech : recruitTechs){
            recruitTechRepository.delete(recruitTech);
        }

        for(Tech tech : techList){
            RecruitTech recruitTech = new RecruitTech(recruit, tech);
            recruitTechRepository.save(recruitTech);
        }
    }
}
