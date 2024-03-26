package com.trendytracker.domain.job.recruit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.trendytracker.domain.job.company.Company;
import com.trendytracker.domain.job.recruittech.RecruitTech;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
  name = "recruit",
  schema = "tech_frontier",
  indexes = {
    @Index(
      name = "idx_company_jobCategory",
      columnList = "company_id, job_category"
    ),
    @Index(name = "idx_url", columnList = "url", unique = true),
    @Index(name = "is_active", columnList = "is_active")
  }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruit {

    @Id
    @GeneratedValue
    @Column(name = "recruit_id")
    private long id;

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

    @OneToMany(
      mappedBy = "recruit",
      fetch = FetchType.LAZY
    )
    private List<RecruitTech> recruitTechs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="company_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Company company;


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
    public void updateJobCategory(String category){
      this.jobCategory = category;
    }

    public void deleteRecruit(){
      this.isActive = false;
    }

    public List<String> getTechList(){
      List<String> techList = new ArrayList<>();
      
      for(RecruitTech recruitTech : this.recruitTechs){
        techList.add(recruitTech.getTech().getTechName());
      }
      return techList;
    }
}
