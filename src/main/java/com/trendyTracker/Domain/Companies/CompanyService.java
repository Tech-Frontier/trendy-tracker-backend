package com.trendyTracker.Domain.Companies;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.NotFoundException;
import org.springframework.stereotype.Service;

import com.trendyTracker.Domain.Companies.Company.CompanyCategory;
import com.trendyTracker.Domain.Companies.VO.CompanyInfo;
import com.trendyTracker.common.Exception.ExceptionDetail.NoResultException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public Company regist(CompanyInfo companyInfo){
        Company newCompany = new Company(companyInfo);

        return companyRepository.save(newCompany);
    }

    public Company findByName(String companyName){
        Company company = companyRepository.findByName(companyName)
            .orElseThrow(() -> new NotFoundException("회사가 존재하지 않습니다"));

        return company;
    }

    public Company updateCategory(String companyName, CompanyCategory category){
        Company company = findByName(companyName);

        return companyRepository.updateCategory(company, category);
    }

    public Company updateGroup(String companyName, String group ){
        Company company = findByName(companyName);

        return companyRepository.updateGroup(company, group);
    }

    public List<CompanyInfo> getCompanyList() throws NoResultException{
        List<Company> companies = companyRepository.findAll();
        if(companies.size() == 0)
            throw new NoResultException("회사 정보가 없습니다");

        return getCompanyInfoList(companies);
    }

    public List<CompanyInfo> getCompanyGroupList(String group) throws NoResultException{
        List<Company> companies = companyRepository.findByGroup(group);
        if(companies.size() == 0)
            throw new NoResultException("회사 정보가 없습니다");
            
        return getCompanyInfoList(companies);
    }

    
    private List<CompanyInfo> getCompanyInfoList(List<Company> companies) {
        List<CompanyInfo> companyInfoList = new ArrayList<>();
        
        for(Company company : companies){
            companyInfoList.add(new CompanyInfo(
                company.getCompanyGroup(),
                company.getCompanyCategory(),
                company.getCompanyName()));
        }

        return companyInfoList;
    }
}
