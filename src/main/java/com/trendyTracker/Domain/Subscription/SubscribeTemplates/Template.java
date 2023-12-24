package com.trendyTracker.Domain.Subscription.SubscribeTemplates;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.trendyTracker.Domain.Subscription.Schedulings.Scheduling;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "template", schema = "techFrontier")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Template {
    
    @Id
    @GeneratedValue
    @Column(name = "template_id")
    private long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "create_time")
    private LocalDateTime createAt;

    @OneToMany(
        mappedBy = "template",
        fetch = FetchType.LAZY,
        cascade = CascadeType.PERSIST
        )
    private List<Scheduling> schedulings = new ArrayList<>();


    public Template(String title, String content){
        this.title = title;
        this.content = content;
        this.createAt = LocalDateTime.now();
    }

}
