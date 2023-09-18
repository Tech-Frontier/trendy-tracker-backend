package com.trendyTracker.Job.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name ="recruit", schema = "public")
@NoArgsConstructor
public class Recruit {
    @Id
    @GeneratedValue
    @Column(name ="recruit_id")
    private long id;

    @OneToMany(mappedBy = "recruit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RecruitTech> urlTechs;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="company_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Company company;

    private String jobCategory;

    @Column(length = 300)
    private String url;

    private String title;

    private LocalDateTime create_time;

    private LocalDateTime updated_time;

    private Boolean is_active;

    // 연관관계 메서드
    public void addRecruit(String url, String title, Company company, String jobCategory){
        this.url = url;
        this.title = title;
        this.company = company;
        this.jobCategory = jobCategory;
        this.create_time = LocalDateTime.now();
        this.is_active = true;
    }
    
    public void updateUrlTechs(List<Tech> techsList){
        List<RecruitTech> newRecruitTechs = new ArrayList<>();

        for (Tech tech : techsList) {
            RecruitTech recruitTech = new RecruitTech();
            recruitTech.addRecruitTech(this, tech);
            newRecruitTechs.add(recruitTech);
        }
        this.urlTechs = newRecruitTechs;
    }

    public List<String> getTechList(){
        List<String> techList = new ArrayList<String>();
        for (RecruitTech recruitTech : urlTechs) {
            techList.add(recruitTech.getTech().getTech_name());
        }
        return techList;
    }

    public void deleteRecruit(){
        this.is_active =false;
    }
}
