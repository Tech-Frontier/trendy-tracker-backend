package com.trendyTracker.domain.AppService;

import java.time.LocalDateTime;

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
@Table(name ="user", schema = "public")
public class User {
    
    @Id
    @GeneratedValue
    @Column(name ="user_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_subscribe_templates_id")
    private UserSubscribeTemplates userSubscribeTemplates;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_subscribe_companies_id")
    private UserSubscribeCompanies userSubscribeCompanies;

    private String email;

    private LocalDateTime create_time;

    // 연관관계 메서드
    public void addUser(String email){
        this.email = email;
        this.create_time = LocalDateTime.now();
    }

   
}
