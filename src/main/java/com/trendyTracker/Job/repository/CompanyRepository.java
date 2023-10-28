package com.trendyTracker.Job.repository;

import java.util.List;
import java.util.Optional;

import com.trendyTracker.Job.domain.Company;
import com.trendyTracker.Job.domain.Company.CompanyCategory;
import com.trendyTracker.Job.domain.Model.CompanyInfo;

public interface CompanyRepository {
    Company registCompany(CompanyInfo companyInfo);

    Company updateCategory(String companyName, CompanyCategory category);
    
    Company updateGroup(String companyName, String group);

    Optional<Company> findCompanyByName(String company);

    List<CompanyInfo> getCompanyAll();

    List<CompanyInfo> getCompanyGroupList(String companyGroup);
}
