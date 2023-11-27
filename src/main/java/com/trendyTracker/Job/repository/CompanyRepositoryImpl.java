package com.trendyTracker.Job.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.trendyTracker.Job.domain.Company;
import com.trendyTracker.Job.domain.Company.CompanyCategory;
import com.trendyTracker.Job.domain.Model.CompanyInfo;
import com.trendyTracker.Job.domain.QCompany;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepository {

  private final EntityManager em;
  private JPAQueryFactory queryFactory;

  @Override
  @Transactional
  public Company registCompany(CompanyInfo companyInfo) {
    /*
     * 'Company' 저장
     */
    queryFactory = new JPAQueryFactory(em);
    QCompany qCompany = QCompany.company;

    var findCompany = queryFactory
      .select(qCompany)
      .from(qCompany)
      .where(qCompany.company_name.eq(companyInfo.companyName()))
      .fetchOne();

    if (findCompany != null) return findCompany;

    Company newCompany = new Company(companyInfo);
    em.persist(newCompany);
    return newCompany;
  }

  @Override
  @Transactional
  public Company updateCategory(String companyName, CompanyCategory category) {
    /*
     * 회사 규모 변경
     */
    queryFactory = new JPAQueryFactory(em);
    QCompany qCompany = QCompany.company;

    Company company = queryFactory
      .selectFrom(qCompany)
      .where(qCompany.company_name.eq(companyName))
      .fetchFirst();

    company.updateCategory(category);

    em.persist(company);
    return company;
  }

  @Override
  @Transactional
  public Company updateGroup(String companyName, String group) {
    /*
     * 회사 그룹 변경
     */
    queryFactory = new JPAQueryFactory(em);
    QCompany qCompany = QCompany.company;

    Company company = queryFactory
      .selectFrom(qCompany)
      .where(qCompany.company_name.eq(companyName))
      .fetchFirst();

    company.updateGroup(group);
    em.persist(company);

    return company;
  }

  @Override
  public List<CompanyInfo> getCompanyAll() {
    queryFactory = new JPAQueryFactory(em);
    QCompany qCompany = QCompany.company;

    var result = queryFactory
      .select(
        qCompany.company_group,
        qCompany.company_category,
        qCompany.company_name
      )
      .from(qCompany)
      .fetch();

    List<CompanyInfo> companyInfoList = result
      .stream()
      .map(tuple ->
        new CompanyInfo(
          tuple.get(qCompany.company_group),
          tuple.get(qCompany.company_category),
          tuple.get(qCompany.company_name)
        )
      )
      .collect(Collectors.toList());

    return companyInfoList;
  }

  @Override
  public List<CompanyInfo> getCompanyGroupList(String companyGroup) {
    /*
     * 그룹사 조회
     */
    queryFactory = new JPAQueryFactory(em);
    QCompany qCompany = QCompany.company;

    var result = queryFactory
      .select(
        qCompany.company_group,
        qCompany.company_category,
        qCompany.company_name
      )
      .from(qCompany)
      .where(qCompany.company_group.eq(companyGroup))
      .fetch();

    List<CompanyInfo> companyInfoList = result
      .stream()
      .map(tuple ->
        new CompanyInfo(
          tuple.get(qCompany.company_group),
          tuple.get(qCompany.company_category),
          tuple.get(qCompany.company_name)
        )
      )
      .collect(Collectors.toList());

    return companyInfoList;
  }

  @Override
  public Optional<Company> findCompanyByName(String company) {
    /*
     * 회사명으로 회사 검색
     */
    queryFactory = new JPAQueryFactory(em);
    QCompany qCompany = QCompany.company;

    Company findCompany = queryFactory
      .selectFrom(qCompany)
      .where(qCompany.company_name.eq(company))
      .fetchFirst();

    if (findCompany == null) return Optional.empty();

    return Optional.of(findCompany);
  }
}
