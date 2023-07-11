package com.trendyTracker.domain.Job;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name ="tech", schema = "public")
public class Tech {

    @Id
    @GeneratedValue
    @Column(name ="tech_id")
    private long id;
    
    @Column(name="tech_name", unique = true)
    private String tech_name;
}
