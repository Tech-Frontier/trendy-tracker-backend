package com.trendytracker.domain.job.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.NotFoundException;
import org.springframework.stereotype.Service;

import com.trendytracker.common.exception.detail.NoResultException;
import com.trendytracker.domain.job.company.Company.CompanyCategory;
import com.trendytracker.domain.job.company.vo.CompanyInfo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public Boolean isExist(String companyName){
        Optional<Company> company = companyRepository.findByCompanyName(companyName);

        if(company.isPresent())
            return true;

        return false;
    }
    
    public Company findByName(String companyName){
        Company company = companyRepository.findByCompanyName(companyName)
        .orElseThrow(() -> new NotFoundException("회사가 존재하지 않습니다"));
        
        return company;
    }

    @Transactional
    public Company regist(CompanyInfo companyInfo){
        Company newCompany = new Company(companyInfo);

        return companyRepository.save(newCompany);
    }

    @Transactional
    public Company updateCategory(String companyName, CompanyCategory category){
        Company company = findByName(companyName);

        company.updateCategory(category);
        return companyRepository.save(company);
    }

    @Transactional
    public Company updateGroup(String companyName, String group ){
        Company company = findByName(companyName);

        company.updateGroup(group);
        return companyRepository.save(company);
    }

    public List<CompanyInfo> getCompanyList() throws NoResultException{
        List<Company> companies = companyRepository.findAll();
        if(companies.isEmpty())
            throw new NoResultException("회사 정보가 없습니다");

        return getCompanyInfoList(companies);
    }

    public List<CompanyInfo> getCompanyGroupList(String group) throws NoResultException{
        List<Company> companies = companyRepository.findByCompanyGroup(group);
        if(companies.isEmpty())
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
