package com.trendytracker.domain.appservice.user;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user" , schema = "tech_frontier" )
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private long id;

    private String email;

    @Column(name = "create_time")
    private LocalDateTime createAt;

    public User(String email){
        this.email = email;
        this.createAt = LocalDateTime.now();
    }
}
