package com.trendyTracker.Domain.Companies;

import java.time.LocalDateTime;

import com.trendyTracker.Domain.Companies.VO.CompanyInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name ="company", schema ="public")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company {
    
    public enum CompanyCategory {
        Series_PreA,
        Series_A,
        Series_B,
        Series_C,
        Series_D,
        Listed,
      }
    
    @Id 
    @GeneratedValue
    @Column(name ="company_id")
    private long id;

    @Column(name ="company_group")
    private String companyGroup;
    
    @Column(name = "company_category")
    private CompanyCategory companyCategory;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    public Company(CompanyInfo companyInfo){
        this.companyGroup = companyInfo.getCompanyGroup();
        this.companyCategory = companyInfo.getCompanyCategory();
        this.companyName = companyInfo.getCompanyName();
        this.updatedTime = LocalDateTime.now();
    }

    // 연관관계 메서드
    public void updateCategory(CompanyCategory category){
        this.companyCategory = category;
        this.updatedTime = LocalDateTime.now();
    }

    public void updateGroup(String group){
        this.companyGroup = group;
        this.updatedTime = LocalDateTime.now();
    }
}
