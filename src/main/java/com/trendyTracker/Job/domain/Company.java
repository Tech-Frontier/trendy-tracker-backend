package com.trendyTracker.Job.domain;

import java.time.LocalDateTime;

import com.trendyTracker.Job.domain.Model.CompanyInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "company" ,schema="public")
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

    private String companyGroup;

    @Enumerated(EnumType.STRING) 
    private CompanyCategory companyCategory;

    @Column(name="company_name", unique = true)
    private String company_name;

    private LocalDateTime updated_time;

    // 연관관계 메서드
    public void addCompany(CompanyInfo companyInfo){
        this.companyGroup = companyInfo.companyGroup();
        this.companyCategory = companyInfo.companyCategory();
        this.company_name = companyInfo.companyName();
        this.updated_time = LocalDateTime.now();
    }

    public void updateCategory(CompanyCategory category){
        this.companyCategory = category;
        this.updated_time = LocalDateTime.now();
    }

    public void updateGroup(String group){
        this.companyGroup = group;
        this.updated_time = LocalDateTime.now();
    }
}
