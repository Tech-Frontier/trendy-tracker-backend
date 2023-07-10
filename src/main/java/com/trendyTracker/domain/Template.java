package com.trendyTracker.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
    
    // private User user;

    private String title;

    @Lob
    private String content;

    // 연관관계 메서드
    public void addTemplate(String title, String content){
        this.title = title;
        this.content = content;
    }
}
