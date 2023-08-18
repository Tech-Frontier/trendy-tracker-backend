package com.trendyTracker.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.webjars.NotFoundException;
import java.io.IOException;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.trendyTracker.Dto.Recruit.RecruitDto;
import com.trendyTracker.common.Exception.ExceptionDetail.NoResultException;
import com.trendyTracker.common.Exception.ExceptionDetail.NotAllowedValueException;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

@SpringBootTest
@Transactional
public class RecruitServiceTest {
    @Autowired
    private RecruitService recruitService;

    // Test 채용공고 
    private final String url = "https://www.owl-dev.me/blog/26";
    private final String company = "owl";
    private final String jobCategory ="fullstack";

   
    @Test
    @DisplayName("채용 공고 등록")
    public void regisitJobPostion() throws NoResultException, IOException{
        // given, when 
        long recruit_id = recruitService.regisitJobPostion(url, company, jobCategory);

        // then 
        Assertions.assertThat(recruit_id).isGreaterThan(0);
    }

    @Test
    @DisplayName("채용 공고 삭제")
    public void deleteJobPostion() throws NoResultException, IOException{
        // given 
        long recruit_id = recruitService.regisitJobPostion(url, company, jobCategory);

        // when 
        recruitService.deleteRecruit(recruit_id);

        // then 
        assertThrows(NotFoundException.class, () -> recruitService.getRecruitInfo(recruit_id));
    }
    
    @Test
    @DisplayName("전체 조회")
    public void getRecruits() throws ValidationException, NoResultException {
        // given 
        String[] companies = {"*"};

        //when
        List<RecruitDto> recruitList = recruitService.getRecruitList(
            companies, null, null, null, null);

        // then
        Assertions.assertThat(recruitList.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("회사별 조회")
    public void getRecruitsByCompanies() throws ValidationException, NoResultException {
        // given 
        String[] companies = {"toss","naver"};

        //when
        List<RecruitDto> recruitList = recruitService.getRecruitList(
            companies, null, null, null, null);

        // then
        Assertions.assertThat(recruitList.size()).isGreaterThan(0);
        Assertions.assertThat(recruitList).extracting(RecruitDto::getCompany).contains("naver");
    }

    @Test
    @DisplayName("회사별 + 직군별 조회")
    public void getRecruitsByCompanies_jobCategory() throws ValidationException, NoResultException {
        // given 
        String[] companies = {"toss","naver"};
        String[] jobCategories = {"backend", "Embeded"};

        //when
        List<RecruitDto> recruitList = recruitService.getRecruitList(
            companies, jobCategories, null, null, null);

        // then
        Assertions.assertThat(recruitList.size()).isGreaterThan(0);
        Assertions.assertThat(recruitList).extracting(RecruitDto::getCompany).contains("naver");
    }

    @Test
    @DisplayName("회사별 + 직군별 + 기술별 조회")
    public void getRecruitsByCompanies_jobCategory_techs() throws ValidationException, NoResultException {
        // given 
        String[] companies = {"toss","naver"};
        String[] jobCategories = {"backend", "robotics"};
        String[] techs ={"Java"};

        // when
        List<RecruitDto> recruitList = recruitService.getRecruitList(
            companies, jobCategories, techs, null, null);

        // then
        Assertions.assertThat(recruitList.size()).isGreaterThan(0);
        Assertions.assertThat(recruitList).extracting(RecruitDto::getCompany).contains("toss");
    }

    @Test
    @DisplayName("전체 페이지별 조회")
    public void getRecruitsBypaging() throws ValidationException, NoResultException {
        // given 
        String[] companies = {"*"};
        int pageNo =2;
        int pageSize=3;

        // when
        List<RecruitDto> recruitList = recruitService.getRecruitList(
            companies, null, null, pageNo, pageSize);

        // then
        Assertions.assertThat(recruitList.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("채용 공고 변경")
    public void changeRecruitTech() throws NoResultException, IOException, NotAllowedValueException{
         // given
        long recruit_id = recruitService.regisitJobPostion(url, company, jobCategory);
        String[] newTechs = {"C#","Python"};

        // when 
        RecruitDto recruitDto = recruitService.updateRecruitTechs(recruit_id,newTechs);

        // when, then
        Assertions.assertThat(recruitDto.getTechList().contains("C#"));
        Assertions.assertThat(recruitDto.getTechList().contains("Python"));
    }

    @Test
    @DisplayName("채용 공고 변경 + 등록되지 않은 기술")
    public void changeRecruitTechsWithWrongTechs() throws ValidationException, NoResultException, IOException, NotAllowedValueException {
         // given
        long recruit_id = recruitService.regisitJobPostion(url, company, jobCategory);
        String[] newTechs = {"C#","Joker"};

        // when, then
        assertThrows(NotAllowedValueException.class, () -> recruitService.updateRecruitTechs(recruit_id,newTechs));
    }
}
