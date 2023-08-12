package com.trendyTracker.domain.AppService;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.trendyTracker.domain.Subscription.Template;

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
@Table(name ="user_subscribe_templates" ,schema="public")
public class UserSubscribeTemplates {
    
    @Id    
    @GeneratedValue
    @Column(name ="user_subscribe_templates_id")
    private long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name ="user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name ="teplate_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Template template;

    @ColumnDefault("true")
    private Boolean is_active;

    private LocalDateTime create_time;

    private LocalDateTime update_time;
}
