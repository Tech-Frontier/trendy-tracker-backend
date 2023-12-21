package com.trendyTracker.Domain.Recruits;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.trendyTracker.Domain.Companies.Company;
import com.trendyTracker.Domain.Techs.Tech;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
  name = "recruit",
  schema = "public",
  indexes = {
    @Index(
      name = "idx_company_jobCategory",
      columnList = "company_id, jobCategory"
    ),
    @Index(name = "idx_url", columnList = "url", unique = true),
  }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruit {

    @Id
    @GeneratedValue
    @Column(name = "recruit_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="company_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Company company;

    @Column(name = "job_category")
    private String jobCategory;

    @Column(length = 300)
    private String url;

    private String title;

    @Column(name ="create_time")
    private LocalDateTime createAt;

    @Column(name = "updated_time")
    private LocalDateTime updateAt;

    @Column(name ="is_active")
    private Boolean isActive;


    public Recruit(
        String url,
        String title,
        Company company,
        String jobCategory
    ){
        this.url = url;
        this.title = title;
        this.company = company;
        this.jobCategory = jobCategory;
        this.createAt = LocalDateTime.now();
        this.isActive = true;
    }

    // 연관관계 메서드
    public void updateUrlTechs(List<Tech> techList){
    }
}
