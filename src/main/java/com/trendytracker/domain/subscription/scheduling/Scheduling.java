package com.trendytracker.domain.subscription.scheduling;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.trendytracker.domain.subscription.subscribetemplate.Template;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "scheduling", schema = "techFrontier")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scheduling {
    
    @Id
    @GeneratedValue
    @Column(name = "scheduling_id")
    private long id;

    @Column(name = "create_time")
    private LocalDateTime createAt;
    
    @Column(name = "is_success")
    @ColumnDefault("false")
    private Boolean isSuccess;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Template template;


    public Scheduling(Template template){
        this.template = template;
        this.createAt = LocalDateTime.now();
        this.isSuccess = false;
    }
}
