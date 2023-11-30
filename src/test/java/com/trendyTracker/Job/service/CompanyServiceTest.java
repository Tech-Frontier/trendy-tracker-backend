package com.trendyTracker.Job.service;

import com.trendyTracker.Job.domain.Company;
import com.trendyTracker.Job.domain.Company.CompanyCategory;
import com.trendyTracker.Job.domain.Model.CompanyInfo;
import com.trendyTracker.Job.repository.CompanyRepository;
import com.trendyTracker.common.Exception.ExceptionDetail.NoResultException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class CompanyServiceTest {

  @Autowired
  private CompanyService companyService;

  @Autowired
  private CompanyRepository companyRepository;

  private final String companyGroup = "toss";
  private final String companyName = "toss_bank";
  private CompanyInfo companyInfo = new CompanyInfo(
    companyGroup,
    CompanyCategory.Series_D,
    companyName
  );

  @Test
  @DisplayName("회사 검색")
  void testFindCompany() {
    // given
    Company registCompany = companyRepository.registCompany(companyInfo);

    // when
    Company findCompany = companyService.findCompany(
      registCompany.getCompany_name()
    );

    // then
    Assertions.assertThat(findCompany.getCompany_name()).isEqualTo(companyName);
    Assertions
      .assertThat(findCompany.getCompany_group())
      .isEqualTo(companyGroup);
    Assertions
      .assertThat(findCompany.getCompany_category())
      .isEqualTo(CompanyCategory.Series_D);
  }

  @Test
  @DisplayName("회사 그룹 검색")
  void testGetCompanyGroup() throws NoResultException {
    // given
    Company registCompany = companyRepository.registCompany(companyInfo);

    // when
    List<CompanyInfo> findCompany = companyService.getCompanyGroup(
      registCompany.getCompany_group()
    );

    // then
    Assertions.assertThat(findCompany.size()).isEqualTo(1);
    Assertions
      .assertThat(findCompany.get(0).companyName())
      .isEqualTo(companyName);
    Assertions
      .assertThat(findCompany.get(0).companyGroup())
      .isEqualTo(companyGroup);
    Assertions
      .assertThat(findCompany.get(0).companyCategory())
      .isEqualTo(CompanyCategory.Series_D);
  }

  @Test
  @DisplayName("회사 전체 검색")
  void testGetCompanyList() throws NoResultException {
    // given
    companyRepository.registCompany(companyInfo);

    // when
    List<CompanyInfo> findCompany = companyService.getCompanyList();

    Assertions.assertThat(findCompany.size()).isGreaterThan(0);
  }

  @Test
  @DisplayName("회사 등록")
  void testRegistCompany() {
    // when
    Company registCompany = companyService.registCompany(companyInfo);

    // then
    Assertions
      .assertThat(registCompany.getCompany_name())
      .isEqualTo(companyName);
    Assertions
      .assertThat(registCompany.getCompany_group())
      .isEqualTo(companyGroup);
    Assertions
      .assertThat(registCompany.getCompany_category())
      .isEqualTo(CompanyCategory.Series_D);
  }

  @Test
  @DisplayName("회사 카테고리 수정")
  void testUpdateCategory() {
    // given
    companyService.registCompany(companyInfo);

    // when
    Company updatedCompany = companyService.updateCategory(
      companyName,
      CompanyCategory.Series_A
    );

    // then
    Assertions
      .assertThat(updatedCompany.getCompany_name())
      .isEqualTo(companyName);
    Assertions
      .assertThat(updatedCompany.getCompany_group())
      .isEqualTo(companyGroup);
    Assertions
      .assertThat(updatedCompany.getCompany_category())
      .isEqualTo(CompanyCategory.Series_A);
  }

  @Test
  @DisplayName("회사 그룹 수정")
  void testUpdateGroup() {
    // given
    companyService.registCompany(companyInfo);

    // when
    Company updatedCompany = companyService.updateGroup(companyName, "naver");

    // then
    Assertions
      .assertThat(updatedCompany.getCompany_name())
      .isEqualTo(companyName);
    Assertions.assertThat(updatedCompany.getCompany_group()).isEqualTo("naver");
    Assertions
      .assertThat(updatedCompany.getCompany_category())
      .isEqualTo(CompanyCategory.Series_D);
  }
}
