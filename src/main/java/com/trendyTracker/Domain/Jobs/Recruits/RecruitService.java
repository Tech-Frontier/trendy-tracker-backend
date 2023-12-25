package com.trendyTracker.Domain.Jobs.Recruits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.openqa.selenium.NotFoundException;
import org.springframework.stereotype.Service;

import com.trendyTracker.Common.Exception.ExceptionDetail.NotAllowedValueException;
import com.trendyTracker.Domain.Jobs.Companies.Company;
import com.trendyTracker.Domain.Jobs.Companies.CompanyRepository;
import com.trendyTracker.Domain.Jobs.Companies.Vo.CompanyInfo;
import com.trendyTracker.Domain.Jobs.RecruitTechs.RecruitTech;
import com.trendyTracker.Domain.Jobs.RecruitTechs.RecruitTechRepository;
import com.trendyTracker.Domain.Jobs.Recruits.Dto.JobScrapInfoDto;
import com.trendyTracker.Domain.Jobs.Techs.Tech;
import com.trendyTracker.Util.JobTotalCntSingleton;
import com.trendyTracker.Util.TechUtils;
import com.trendyTracker.Util.UrlReader;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private final CompanyRepository companyRepository;
    private final RecruitRepository recruitRepository;
    private final RecruitTechRepository recruitTechRepository;

    JobTotalCntSingleton recruitSingleton = JobTotalCntSingleton.getInstance();

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
        long cnt = recruitRepository.count();
        // Singleton 데이터 삽입
        recruitSingleton.setTotalCnt(cnt);

        return cnt;
    }
    

    @Transactional
    public Recruit registRecruit(String url, String company, String jobCategory) throws IOException{
    /*
     * url , company, jobCategory 를 활용해 채용공고 등록
     */
        JobScrapInfoDto urlContent = UrlReader.getUrlContent(url);

        if(urlContent.techSet().isEmpty())
            throw new NoResultException("해당 url 에서 tech 가 발견되지 않았습니다");
        
        Company findCompany = companyRepository.findByCompanyName(company)
            .orElseGet(() -> {
                CompanyInfo companyInfo = new CompanyInfo(null, null, company);
                return companyRepository.save(new Company(companyInfo));
            });
        
        recruitSingleton.increaseCnt();
        
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
        getTotalCnt();
    }

    @Transactional
    public Recruit updaRecruitTechs(long id, String[] techs) throws NotAllowedValueException{
    /*
     * 채용공고 기술 스택 변경
     */
        Recruit recruit = getRecruit(id);
        if(TechUtils.isTechNotExist(techs))
            throw new NotAllowedValueException("존재하지 않는 기술이 존재합니다");

        List<Tech> techList = TechUtils.makeTechs(techs);

        saveRecruitTechs(techList,recruit);        
        return recruit;
    }

    @Transactional
    public Recruit updateRecruitInfo(String url) throws IOException{
    /*
     * 기존 채용공고 새로 Scrapping
     */
        Recruit recruit = getRecruit(url);
        JobScrapInfoDto urlContent = UrlReader.getUrlContent(url);

        if(urlContent.techSet().isEmpty())
            throw new NoResultException("해당 url 에서 tech 가 발견되지 않았습니다");
        
        saveRecruitTechs(urlContent, recruit);        
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
    private void saveRecruitTechs(List<Tech> techList, Recruit recruit) {
    /*
     * 채용기술 저장
     */
        for(Tech tech : techList){
            RecruitTech recruitTech = new RecruitTech(recruit, tech);
            recruitTechRepository.save(recruitTech);
        }
    }
}
