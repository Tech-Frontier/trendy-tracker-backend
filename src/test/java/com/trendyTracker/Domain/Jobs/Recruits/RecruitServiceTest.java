package com.trendyTracker.Domain.Jobs.Recruits;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.openqa.selenium.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.trendyTracker.Common.Exception.ExceptionDetail.NoResultException;
import com.trendyTracker.Common.Exception.ExceptionDetail.NotAllowedValueException;
import com.trendyTracker.Domain.Jobs.Techs.Tech;
import com.trendyTracker.Domain.Jobs.Techs.Tech.TechType;
import com.trendyTracker.Util.TechListSingleton;

import jakarta.transaction.Transactional;


@SpringBootTest
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
public class RecruitServiceTest {
    @Autowired
    private RecruitService recruitService;    
  
    static String url = "https://www.owl-dev.me/blog/72";
    static String title = "Docker + RaspberryPi 환경에서 Selenium 동작시키기";
    static String company = "paperCompany";
    static String jobCategory = "backend";
    static long recruit_id = 0L;

    @BeforeAll
    public void setUpBeforeClass() throws IOException, NoResultException {
        List<Tech> techList = new ArrayList<>();
        techList.add(new Tech("Java", TechType.LANGUAGE));
        techList.add(new Tech("C#", TechType.LANGUAGE));
        techList.add(new Tech("Spring", TechType.FRAMEWORK));
        techList.add(new Tech("Docker", TechType.TOOLS));
        
        TechListSingleton instance = TechListSingleton.getInstance();
        instance.setTechList(techList);

        recruit_id = recruitService.registRecruit(url, company, jobCategory).getId();
    }

    @AfterAll
    public void deleteTestRecruit(){
        if(recruitService.isExsit(url))
            recruitService.deleteRecruit(recruit_id);
    }

    @Test
    void testRegistRecruit() throws IOException, NoResultException {
        // given

        // when
        Recruit registRecruit = recruitService.registRecruit(url, company, jobCategory);

        // then 
        assertNotNull(registRecruit);
        assertEquals(registRecruit.getTitle(), title);
        assertEquals(registRecruit.getCompany().getCompanyName(), company);
        assertEquals(registRecruit.getJobCategory(), jobCategory);
    }

    @Test
    void getRecruitByIdTest() throws IOException {
        // given

        // when
        Recruit recruit = recruitService.getRecruit(recruit_id);

        // then 
        assertNotNull(recruit);
        assertEquals(recruit.getTitle(), title);
        assertEquals(recruit.getCompany().getCompanyName(), company);
        assertEquals(recruit.getJobCategory(), jobCategory);
    }

    @Test
    void testDeleteRecruit() throws IOException {
        // given

        // when 
        recruitService.deleteRecruit(recruit_id);

        // then 
        assertThrows(NotFoundException.class,() 
            -> recruitService.getRecruit(recruit_id));
    }

    @Test
    void testGetRecruitById() throws IOException {
        // given

        // when 
        Recruit recruit = recruitService.getRecruit(recruit_id);

        // then
        assertNotNull(recruit);
        assertEquals(recruit.getTitle(), title);
        assertEquals(recruit.getCompany().getCompanyName(), company);
        assertEquals(recruit.getJobCategory(), jobCategory);
    }

    @Test
    void testGetRecruitByUrl() throws IOException {
        // given

        // when 
        Recruit recruit = recruitService.getRecruit(url);

        // then
        assertNotNull(recruit);
        assertEquals(recruit.getTitle(), title);
        assertEquals(recruit.getCompany().getCompanyName(), company);
        assertEquals(recruit.getJobCategory(), jobCategory);
    }

    @Test
    void testGetRecuitList() throws IOException {
        // given
        String[] companies = {company};

        // when
        List<Recruit> recuitList = recruitService.getRecuitList(companies, null, null, null, null);

        // then 
        assertTrue(recuitList.size() > 0);
        // assertTrue(recuitList.get(0).getTechList().contains("Docker"));
    }

    @Test
    void testGetTotalCnt() throws IOException {
        // given

        // when
        long totalCnt = recruitService.getTotalCnt();

        // then 
        assertTrue(totalCnt > 0);
    }

    @Test
    void testIsExsit() throws IOException {
        // given

        // when 
        Boolean exsit = recruitService.isExsit(url);

        // then 
        assertTrue(exsit);
    }

    @Test
    void testUpdaRecruitTechs() throws IOException, NotAllowedValueException {
        // given
        String[] techs = {"Java","C#","Spring"};

        // when 
        Recruit updaRecruit = recruitService.updaRecruitTechs(recruit_id, techs);

        // then 
        assertTrue(updaRecruit.getTechList().contains("Java"));
        assertTrue(updaRecruit.getTechList().contains("Spring"));
    }

    @Test
    void testUpdaRecruitTechsWithNotExistStack() throws IOException, NotAllowedValueException {
        // given
        String[] techs = {"Kafka"};

        // when, then 
        assertThrows(NotAllowedValueException.class, 
            () -> recruitService.updaRecruitTechs(recruit_id, techs));
    }

    @Test
    void testUpdateRecruitInfo() throws IOException, NoResultException {
        // given

        // when
        Recruit recruit = recruitService.updateRecruitInfo(url);

        // then
        assertNotNull(recruit);
        assertEquals(recruit.getTitle(), title);
        assertEquals(recruit.getCompany().getCompanyName(), company);
        assertEquals(recruit.getJobCategory(), jobCategory);
    }

    @Test
    void testUpdateRecruitInfoWithNotExistRecruit() throws IOException {
        // given
        String testUrl ="www.naver.com";

        // when, then 
        assertThrows(
                NotFoundException.class, 
            () -> recruitService.updateRecruitInfo(testUrl));
    }
}
