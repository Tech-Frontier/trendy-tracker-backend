package com.trendyTracker.Job.domain;

import com.trendyTracker.Job.domain.Model.CompanyInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "company", schema = "public")
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
  @Column(name = "company_id")
  private long id;

  private String company_group;

  @Enumerated(EnumType.STRING)
  private CompanyCategory company_category;

  @Column(name = "company_name", unique = true)
  private String company_name;

  private LocalDateTime updated_time;

  public Company(CompanyInfo companyInfo) {
    this.company_group = companyInfo.companyGroup();
    this.company_category = companyInfo.companyCategory();
    this.company_name = companyInfo.companyName();
    this.updated_time = LocalDateTime.now();
  }

  // 연관관계 메서드
  public void updateCategory(CompanyCategory category) {
    this.company_category = category;
    this.updated_time = LocalDateTime.now();
  }

  public void updateGroup(String group) {
    this.company_group = group;
    this.updated_time = LocalDateTime.now();
  }
}
