package com.trendyTracker.Job.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "company" ,schema="public")
public class Company {
    @Id
    @GeneratedValue
    @Column(name ="company_id")
    private long id;

    @Column(name="company_name", unique = true)
    private String company_name;

    private LocalDateTime updated_time;

    // 연관관계 메서드
    public void addCompany(String company_name){
        this.company_name = company_name;
        this.updated_time = LocalDateTime.now();
    }
}
