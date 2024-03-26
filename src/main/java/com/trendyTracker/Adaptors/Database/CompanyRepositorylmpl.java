package com.trendytracker.Adaptors.Database;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

import com.trendytracker.Domain.Jobs.Companies.Company;
import com.trendytracker.Domain.Jobs.Companies.CompanyRepositoryCustom;
import com.trendytracker.Domain.Jobs.Companies.QCompany;


@Repository
public class CompanyRepositorylmpl implements CompanyRepositoryCustom {
    private final EntityManager em;
    private JPAQueryFactory factory;

    public CompanyRepositorylmpl(EntityManager entityManager){
        this.em = entityManager;
        this.factory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Company> findByCompanyName(String companyName) {
    /*
     * 회사명으로 검색
     */
        QCompany qCompany = QCompany.company;

        Company company = factory.selectFrom(qCompany)
                        .where(qCompany.companyName.eq(companyName))
                        .fetchFirst();

        return Optional.ofNullable(company);
    }

    @Override
    public List<Company> findByCompanyGroup(String group) {
    /*
     * 회사그룹으로 검색
     */
        QCompany qCompany = QCompany.company;

        return factory.selectFrom(qCompany)
            .where(qCompany.companyGroup.eq(group))
            .fetch();
    }
}
