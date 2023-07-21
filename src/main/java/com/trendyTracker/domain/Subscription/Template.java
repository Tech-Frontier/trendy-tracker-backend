package com.trendyTracker.domain.Subscription;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name ="template", schema = "public")
public class Template {

    @Id
    @GeneratedValue
    @Column(name ="template_id")
    private long id;

    @OneToMany(mappedBy = "template", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Scheduling> schedulings;

    private String title;

    @Column(columnDefinition = "TEXT") 
    private String content;

    private LocalDateTime create_time;

    // 연관관계 메서드
    public void addTemplate(String title, String content){
        this.title = title;
        this.content = content;
        this.create_time = LocalDateTime.now();
    }
}
