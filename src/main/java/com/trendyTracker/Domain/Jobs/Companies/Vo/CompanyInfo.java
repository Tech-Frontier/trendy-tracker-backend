package com.trendyTracker.Domain.Jobs.Companies.Vo;

import java.util.Objects;

import com.trendyTracker.Domain.Jobs.Companies.Company.CompanyCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CompanyInfo {
    @NotNull
    @Schema(description = "Company Group", example = "Naver")
    private final String companyGroup;

    @NotNull
    @Schema(description = "Company size", example = "Series_C")
    private final CompanyCategory companyCategory;

    @NotNull
    @Schema(description = "Company Name", example = "Naver_Webtoon")
    private final String companyName;

    public CompanyInfo(String companyGroup, CompanyCategory companyCategory, String companyName) {
        this.companyGroup = companyGroup;
        this.companyCategory = companyCategory;
        this.companyName = companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyInfo)) return false;

        CompanyInfo that = (CompanyInfo) o;
        return Objects.equals(companyGroup, that.companyGroup) &&
                Objects.equals(companyName, that.companyName) &&
                companyCategory == that.companyCategory ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyGroup, companyCategory, companyName);
    }

}
