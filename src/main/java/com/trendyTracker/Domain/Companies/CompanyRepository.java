package com.trendyTracker.Domain.Companies;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trendyTracker.Domain.Companies.Company.CompanyCategory;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    
    Optional<Company> findByName(String companyName);

    Company updateCategory(Company company, CompanyCategory category);

    Company updateGroup(Company company, String group);

    List<Company> findByGroup(String group);
}
