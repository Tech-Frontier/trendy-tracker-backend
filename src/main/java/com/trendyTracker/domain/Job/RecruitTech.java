package com.trendyTracker.domain.Job;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name ="recruit_tech", schema = "public")
public class RecruitTech {

    @Id
    @GeneratedValue
    @Column(name ="recruit_tech_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="recruit_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Recruit recruit;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name ="tech_name")
    private Tech tech;

    // 연관관계 메서드
    public void addRecruitTech(Recruit recruit, Tech tech){
        this.recruit = recruit;
        this.tech = tech;
    }
}
