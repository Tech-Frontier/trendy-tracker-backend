package com.trendyTracker.util;

import com.trendyTracker.Job.domain.Company.CompanyCategory;

public class CompanyUtils {

    public static CompanyCategory makeCompanyCategory(String companyCategory){
        switch (companyCategory) {
            case "Series_PreA":
                return CompanyCategory.Series_PreA;
            case "Series_A":
                return CompanyCategory.Series_A;
            case "Series_B":
                return CompanyCategory.Series_B;
            case "Series_C":
                return CompanyCategory.Series_C;
            case "Series_D":
                return CompanyCategory.Series_D;
            case "Listed":
                return CompanyCategory.Listed;
            default:
                throw new IllegalArgumentException("올바르지 않은 CompanyCategory 값: " + companyCategory);
        }
    }
}   
