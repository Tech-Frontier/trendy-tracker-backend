package com.trendytracker.Domain.AppService.UserSubscribeCompanies;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.trendytracker.Domain.AppService.Users.User;
import com.trendytracker.Domain.Jobs.Companies.Company;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_subscribe_companies", schema = "tech_frontier")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSubscribeCompanies {
    
    @Id
    @GeneratedValue
    @Column(name = "user_subscribe_companies_id")
    private long id;

    @Column(name = "is_active")
    @ColumnDefault("true")
    private Boolean isActive;

    @Column(name = "create_time")
    private LocalDateTime createAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Company company;

    public UserSubscribeCompanies(User user, Company company){
        this.user = user;
        this.company = company;
        this.createAt = LocalDateTime.now();
        this.isActive = true;
    }
}
