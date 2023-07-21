package com.trendyTracker.domain.AppService;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.trendyTracker.domain.Job.Company;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="user_subscribe_companies" ,schema="public")
public class UserSubscribeCompanies {
    @Id    
    @GeneratedValue
    @Column(name ="user_subscribe_companies_id")
    private long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private Company company;

    @ColumnDefault("true")
    private Boolean is_active;

    private LocalDateTime create_time;
}
