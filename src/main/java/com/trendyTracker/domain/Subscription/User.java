package com.trendyTracker.domain.Subscription;

import java.time.LocalDateTime;
import java.util.List;

import com.trendyTracker.domain.AppService.Company;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy="user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Template> templates;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_subscribe_companies", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
    private List<Company> companies;

    private String email;

    private LocalDateTime create_time;

    // 연관관계 메서드
    public void addUser(String email){
        this.email = email;
        this.create_time = LocalDateTime.now();
    }

    public void addTemplate(Template templates){
        templates.setUser(this);
        this.templates.add(templates);
    }
}
