package com.trendyTracker.Subscription.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
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
@Table(name ="Scheduling", schema = "public")
public class Scheduling {
    
    @Id
    @GeneratedValue
    @Column(name ="scheduling_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="template_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Template template;

    private LocalDateTime create_time;

    @ColumnDefault("false")
    private Boolean isSuccess;
}
