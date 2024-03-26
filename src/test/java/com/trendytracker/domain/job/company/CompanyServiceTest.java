package com.trendytracker.domain.job.company;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.trendytracker.common.exception.detail.NoResultException;
import com.trendytracker.domain.job.company.Company.CompanyCategory;
import com.trendytracker.domain.job.company.vo.CompanyInfo;

import jakarta.transaction.Transactional;

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
  void testFindByName() {
    // given
    var company = companyRepository.save(new Company(companyInfo));

    // when
    Company findCompany = companyService.findByName(
      company.getCompanyName()
    );

    // then
    assertEquals(findCompany.getCompanyName(), companyInfo.getCompanyName());
    assertEquals(findCompany.getCompanyCategory(), companyInfo.getCompanyCategory());
    assertEquals(findCompany.getCompanyGroup(), companyInfo.getCompanyGroup());
  }

  @Test
  void testGetCompanyGroup() throws NoResultException {
    // given
    var company = companyRepository.save(new Company(companyInfo));

    // when
    List<CompanyInfo> companies = companyService.getCompanyGroupList(
      company.getCompanyGroup()
    );

    // then
    assertEquals(companies.size(), 1);
    assertEquals(companies.get(0).getCompanyName(), companyInfo.getCompanyName());
    assertEquals(companies.get(0).getCompanyGroup(), companyInfo.getCompanyGroup());
    assertEquals(companies.get(0).getCompanyCategory(), companyInfo.getCompanyCategory());
  }

  @Test
  void testGetCompanyList() throws NoResultException {
    // given
    companyRepository.save(new Company(companyInfo));

    // when
    List<CompanyInfo> findCompany = companyService.getCompanyList();

    // then
    Assertions.assertThat(findCompany.size()).isGreaterThan(0);
  }

  @Test
  void testSave() {
    // when
    Company company = companyRepository.save(new Company(companyInfo));

    // then
    assertEquals(company.getCompanyName(), companyInfo.getCompanyName());
    assertEquals(company.getCompanyCategory(), companyInfo.getCompanyCategory());
    assertEquals(company.getCompanyGroup(), companyInfo.getCompanyGroup());
  }

  @Test
  void testUpdateCategory() {
    // given
    Company company = companyRepository.save(new Company(companyInfo));

    // when
    companyService.updateCategory(
      companyName,
      CompanyCategory.Series_A
    );

    // then
    assertEquals(company.getCompanyName(), companyInfo.getCompanyName());
    assertEquals(company.getCompanyGroup(), companyInfo.getCompanyGroup());
    assertEquals(company.getCompanyCategory(), CompanyCategory.Series_A);
  }

  @Test
  void testUpdateGroup() {
    // given
    Company company = companyRepository.save(new Company(companyInfo));

    // when
    companyService.updateGroup(companyName, "naver");

    // then
    assertEquals(company.getCompanyName(), companyInfo.getCompanyName());
    assertEquals(company.getCompanyCategory(), companyInfo.getCompanyCategory());
    assertEquals(company.getCompanyGroup(), "naver");
  }
}
