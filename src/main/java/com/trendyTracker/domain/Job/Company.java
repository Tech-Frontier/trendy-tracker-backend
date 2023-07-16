package com.trendyTracker.domain.Job;

import java.time.LocalDateTime;

import com.trendyTracker.domain.AppService.UserSubscribeCompanies;

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
@Table(name = "company" ,schema="public")
public class Company {
    @Id
    @GeneratedValue
    @Column(name ="company_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_subscribe_companies_id")
    private UserSubscribeCompanies userSubscribeCompanies;

    @Column(name="company_name", unique = true)
    private String company_name;

    private LocalDateTime updated_time;
}
