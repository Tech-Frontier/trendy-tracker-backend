package com.trendyTracker.domain.AppService;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.trendyTracker.domain.Subscription.Template;

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
@Table(name ="user_subscribe_templates" ,schema="public")
public class UserSubscribeTemplates {
    
    @Id    
    @GeneratedValue
    @Column(name ="user_subscribe_templates_id")
    private long id;

    @OneToMany(mappedBy = "userSubscribeTemplates", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> users;

    @OneToMany(mappedBy = "userSubscribeTemplates", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Template> templates;

    @ColumnDefault("true")
    private Boolean is_active;

    private LocalDateTime create_time;

    private LocalDateTime update_time;
}
