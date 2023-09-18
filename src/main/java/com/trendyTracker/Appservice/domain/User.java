package com.trendyTracker.Appservice.domain;

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
@Table(name ="user", schema = "public")
public class User {
    
    @Id
    @GeneratedValue
    @Column(name ="user_id")
    private long id;

    private String email;

    private String password;

    private LocalDateTime create_time;

    // 연관관계 메서드
    public void addUser(String email, String password){
        this.email = email;
        this.password = password;
        this.create_time = LocalDateTime.now();
    }
}
