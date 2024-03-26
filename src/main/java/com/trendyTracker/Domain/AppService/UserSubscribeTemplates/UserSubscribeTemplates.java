package com.trendytracker.Domain.AppService.UserSubscribeTemplates;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.trendytracker.Domain.AppService.Users.User;

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
@Table(name = "user_subscribe_templates" , schema = "tech_frontier")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSubscribeTemplates {
    
    @Id
    @GeneratedValue
    @Column(name = "user_subscribe_templates_id")
    private long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "is_active")
    @ColumnDefault("true")
    private Boolean isActive;

    @Column(name = "create_time")
    private LocalDateTime createAt;

    @Column(name = "update_time")
    private LocalDateTime updateAt;

    public UserSubscribeTemplates(User user){
        this.user = user;
        this.createAt = LocalDateTime.now();
    }
}
