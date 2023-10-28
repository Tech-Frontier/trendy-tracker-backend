package com.trendyTracker.Job.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.trendyTracker.Job.domain.Company;
import com.trendyTracker.Job.domain.Company.CompanyCategory;
import com.trendyTracker.Job.domain.Model.CompanyInfo;
import com.trendyTracker.Job.repository.CompanyRepository;
import com.trendyTracker.common.Exception.ExceptionDetail.NoResultException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public Company findCompany(String companyName){
        Optional<Company> company = companyRepository.findCompanyByName(companyName);
        company.orElseThrow(() -> new NotFoundException("회사가 존재하지 않습니다"));

        return company.get();
    }

    public Company registCompany(CompanyInfo companyInfo){
        Company company = companyRepository.registCompany(companyInfo);

        return company;
    }

    public Company updateCategory(String companyName, CompanyCategory category){
        var findCompany = findCompany(companyName);
        var company = companyRepository.updateCategory(findCompany.getCompany_name(), category);

        return company;
    }

    public Company updateGroup(String companyName, String group){
        var findCompany = findCompany(companyName);
        Company company = companyRepository.updateGroup(findCompany.getCompany_name(), group);

        return company;
    }

    public List<CompanyInfo> getCompanyList() throws NoResultException{
        List<CompanyInfo> companyAll = companyRepository.getCompanyAll();
        if(companyAll.size() == 0)
            throw new NoResultException("회사 정보가 없습니다");

        return companyAll;
    }

    public List<CompanyInfo> getCompanyGroup(String companyGroup) throws NoResultException{
        List<CompanyInfo> groupList = companyRepository.getCompanyGroupList(companyGroup);
        if(groupList.size() == 0)
            throw new NoResultException("회사 정보가 없습니다");

        return groupList;
    }
}
