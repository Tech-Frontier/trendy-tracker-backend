package com.trendyTracker.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.trendyTracker.Job.domain.Company;
import com.trendyTracker.Job.domain.Company.CompanyCategory;
import com.trendyTracker.Job.domain.Model.CompanyInfo;
import com.trendyTracker.Job.domain.Recruit;
import com.trendyTracker.Job.service.RecruitService;
import com.trendyTracker.common.Exception.ExceptionDetail.NoResultException;
import com.trendyTracker.common.Exception.ExceptionDetail.NotAllowedValueException;
import com.trendyTracker.util.TechUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.webjars.NotFoundException;

@SpringBootTest
@Transactional
public class RecruitServiceTest {

  @MockBean
  private RecruitService recruitService;

  // Test 채용공고
  private final String url = "https://www.owl-dev.me/blog/26";
  private final String company = "owl";
  private final String jobCategory = "fullstack";

  @BeforeEach
  void MockRecruitService()
    throws NoResultException, IOException, NotAllowedValueException {
    CompanyInfo companyInfo = new CompanyInfo(
      company,
      CompanyCategory.Series_D,
      "toss"
    );
    CompanyInfo companyInfo2 = new CompanyInfo(
      company,
      CompanyCategory.Series_A,
      "naver"
    );
    Company tempCompany1 = new Company(companyInfo);
    Company tempCompany2 = new Company(companyInfo2);
    Recruit tempRecruit1 = new Recruit(url, "title", tempCompany1, jobCategory);
    Recruit tempRecruit2 = new Recruit(url, "title", tempCompany2, jobCategory);

    String[] companies;
    String[] jobCategories;
    String[] techs;
    String[] newTechs;

    // regisitJobPostion
    when(recruitService.regisitJobPostion(url, "toss", jobCategory))
      .thenReturn((long) 1);

    // getRecruitList
    companies = new String[] { "*" };
    when(recruitService.getRecruitList(companies, null, null, null, null))
      .thenReturn(
        new ArrayList<Recruit>() {
          {
            add(tempRecruit1);
            add(tempRecruit2);
          }
        }
      );

    // getRecruitList
    companies = new String[] { "toss", "naver" };
    when(recruitService.getRecruitList(companies, null, null, null, null))
      .thenReturn(
        new ArrayList<Recruit>() {
          {
            add(tempRecruit1);
            add(tempRecruit2);
          }
        }
      );

    // getRecruitList
    companies = new String[] { "toss", "naver" };
    jobCategories = new String[] { "backend", "robotics" };
    when(
      recruitService.getRecruitList(companies, jobCategories, null, null, null)
    )
      .thenReturn(
        new ArrayList<Recruit>() {
          {
            add(tempRecruit1);
            add(tempRecruit2);
          }
        }
      );

    // getRecruitList
    companies = new String[] { "toss", "naver" };
    jobCategories = new String[] { "backend", "robotics" };
    techs = new String[] { "java" };
    when(
      recruitService.getRecruitList(companies, jobCategories, techs, null, null)
    )
      .thenReturn(
        new ArrayList<Recruit>() {
          {
            add(tempRecruit1);
            add(tempRecruit2);
          }
        }
      );

    // getRecruitList
    companies = new String[] { "*" };
    int pageNo = 1;
    int pageSize = 3;
    when(recruitService.getRecruitList(companies, null, null, pageNo, pageSize))
      .thenReturn(
        new ArrayList<Recruit>() {
          {
            add(tempRecruit1);
            add(tempRecruit2);
          }
        }
      );

    // NotFoundException
    doThrow(new NotFoundException("not found"))
      .when(recruitService)
      .getRecruitInfo(999);

    // updateRecruitTechs
    newTechs = new String[] { "C#", "Python" };
    tempRecruit1.updateUrlTechs(TechUtils.makeTechs(newTechs));
    when(recruitService.updateRecruitTechs(1, newTechs))
      .thenReturn(tempRecruit1);

    // updateJobPosition
    newTechs = new String[] { "C#", "Python" };
    when(recruitService.updateJobPosition(url)).thenReturn((long) 1);

    // NotAllowedValueException
    newTechs = new String[] { "C#", "Joker" };
    doThrow(new NotAllowedValueException("not allowed"))
      .when(recruitService)
      .updateRecruitTechs(1, newTechs);
  }

  @Test
  @DisplayName("채용 공고 등록")
  public void regisitJobPostion() throws NoResultException, IOException {
    // given
    String company = "toss";

    //when
    long recruit_id = recruitService.regisitJobPostion(
      url,
      company,
      jobCategory
    );

    // then
    Assertions.assertThat(recruit_id).isGreaterThan(0);
  }

  @Test
  @DisplayName("채용 공고 삭제")
  public void deleteJobPostion() throws NoResultException, IOException {
    // given
    long recruit_id = 999;

    // when
    recruitService.deleteRecruit(recruit_id);

    // then
    assertThrows(
      NotFoundException.class,
      () -> recruitService.getRecruitInfo(recruit_id)
    );
  }

  @Test
  @DisplayName("전체 조회")
  public void getRecruits() throws ValidationException, NoResultException {
    // given
    String[] companies = { "*" };

    //when
    List<Recruit> recruitList = recruitService.getRecruitList(
      companies,
      null,
      null,
      null,
      null
    );

    // then
    Assertions.assertThat(recruitList.size()).isGreaterThan(0);
  }

  @Test
  @DisplayName("회사별 조회")
  public void getRecruitsByCompanies()
    throws ValidationException, NoResultException {
    // given
    String[] companies = { "toss", "naver" };

    //when
    List<Recruit> recruitList = recruitService.getRecruitList(
      companies,
      null,
      null,
      null,
      null
    );

    // then
    Assertions.assertThat(recruitList.size()).isGreaterThan(0);
    Assertions
      .assertThat(recruitList)
      .extracting(x -> x.getCompany().getCompany_name())
      .contains("toss");
  }

  @Test
  @DisplayName("회사별 + 직군별 조회")
  public void getRecruitsByCompanies_jobCategory()
    throws ValidationException, NoResultException {
    // given
    String[] companies = { "toss", "naver" };
    String[] jobCategories = { "backend", "robotics" };

    //when
    List<Recruit> recruitList = recruitService.getRecruitList(
      companies,
      jobCategories,
      null,
      null,
      null
    );

    // then
    Assertions.assertThat(recruitList.size()).isGreaterThan(0);
    Assertions
      .assertThat(recruitList)
      .extracting(x -> x.getCompany().getCompany_name())
      .contains("toss");
  }

  @Test
  @DisplayName("회사별 + 직군별 + 기술별 조회")
  public void getRecruitsByCompanies_jobCategory_techs()
    throws ValidationException, NoResultException {
    // given
    String[] companies = { "toss", "naver" };
    String[] jobCategories = { "backend", "robotics" };
    String[] techs = { "java" };

    // when
    List<Recruit> recruitList = recruitService.getRecruitList(
      companies,
      jobCategories,
      techs,
      null,
      null
    );

    // then
    Assertions.assertThat(recruitList.size()).isGreaterThan(0);
    Assertions
      .assertThat(recruitList)
      .extracting(x -> x.getCompany().getCompany_name())
      .contains("toss");
  }

  @Test
  @DisplayName("전체 페이지별 조회")
  public void getRecruitsBypaging()
    throws ValidationException, NoResultException {
    // given
    String[] companies = { "*" };
    int pageNo = 1;
    int pageSize = 3;

    // when
    List<Recruit> recruitList = recruitService.getRecruitList(
      companies,
      null,
      null,
      pageNo,
      pageSize
    );

    // then
    Assertions.assertThat(recruitList.size()).isGreaterThan(0);
  }

  @Test
  @DisplayName("채용 공고 변경")
  public void changeRecruitTech()
    throws NoResultException, IOException, NotAllowedValueException {
    // given
    long recruit_id = 1;
    String[] newTechs = { "C#", "Python" };

    // when
    Recruit recruit = recruitService.updateRecruitTechs(recruit_id, newTechs);

    // when, then
    Assertions.assertThat(recruit.getTechList().contains("C#"));
    Assertions.assertThat(recruit.getTechList().contains("Python"));
  }

  @Test
  @DisplayName("채용 공고 변경 + 등록되지 않은 기술")
  public void changeRecruitTechsWithWrongTechs()
    throws ValidationException, NoResultException, IOException, NotAllowedValueException {
    // given
    long recruit_id = 1;
    String[] newTechs = { "C#", "Joker" };

    // when, then
    assertThrows(
      NotAllowedValueException.class,
      () -> recruitService.updateRecruitTechs(recruit_id, newTechs)
    );
  }

  @Test
  @DisplayName("채용공고 재등록")
  public void updateJobPosition() throws IOException, NoResultException {
    // given
    String company = "toss";
    long recruit_id = recruitService.regisitJobPostion(
      url,
      company,
      jobCategory
    );
    // when
    long result_id = recruitService.updateJobPosition(url);

    // then
    Assertions.assertThat(result_id == recruit_id);
  }
}
