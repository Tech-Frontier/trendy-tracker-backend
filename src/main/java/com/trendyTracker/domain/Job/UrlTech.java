package com.trendyTracker.domain.Job;

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
@Table(name ="url_tech", schema = "public")
public class UrlTech {

    @Id
    @GeneratedValue
    @Column(name ="url_tech_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="recruit_id")
    private Recruit recruit;

    @Column(name="tech_name", unique = true)
    private String tech_name;
}
