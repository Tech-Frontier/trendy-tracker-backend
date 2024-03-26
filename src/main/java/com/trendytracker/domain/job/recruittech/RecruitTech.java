package com.trendytracker.domain.job.recruittech;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.trendytracker.domain.job.tech.Tech;
import com.trendytracker.domain.job.recruit.Recruit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name ="recruit_tech", schema = "tech_frontier")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitTech {
    
    @Id
    @GeneratedValue
    @Column(name = "recruit_tech_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="recruit_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Recruit recruit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="tech_name")
    private Tech tech;

    public RecruitTech(Recruit recruit, Tech tech){
        this.recruit =recruit;
        this.tech = tech;
    }
}
