package com.trendyTracker.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name ="tech", schema = "public")
public class Tech {
    
    @Id
    private String name;
}
