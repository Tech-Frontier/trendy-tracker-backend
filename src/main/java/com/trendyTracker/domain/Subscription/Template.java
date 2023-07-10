package com.trendyTracker.domain.Subscription;

import java.util.List;

import com.trendyTracker.domain.AppService.Company;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;

    @OneToMany(mappedBy = "template", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Scheduling> schedulings;

    private String title;

    @Column(columnDefinition = "TEXT") 
    private String content;

    // 연관관계 메서드
    public void addTemplate(User user, String title, String content){
        this.user =user;
        this.title = title;
        this.content = content;

        if (user.getTemplates() != null)
            user.getTemplates().add(this);
    }
}
