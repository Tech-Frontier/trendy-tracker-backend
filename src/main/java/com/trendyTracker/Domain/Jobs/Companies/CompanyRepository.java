package com.trendytracker.Domain.Jobs.Companies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> , CompanyRepositoryCustom{
    
}
