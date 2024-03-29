package com.trendytracker.domain.job.company;

import java.util.List;
import java.util.Optional;


public interface CompanyRepositoryCustom {
    Optional<Company> findByCompanyName(String companyName);

    List<Company> findByCompanyGroup(String group);
}
