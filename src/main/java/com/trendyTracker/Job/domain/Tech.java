package com.trendyTracker.Job.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name ="tech", schema = "public")
@NoArgsConstructor
public class Tech {

    public enum TechType {
        LANGUAGE,
        DATABASE,
        FRAMEWORK,
        LIBRARIES,
        TOOLS,
        IDE,
        OTHER
    }

    @Id
    @Column(name="tech_name", unique = true)
    private String tech_name;

    @Enumerated(EnumType.STRING) 
    private TechType type;

    // 연관관계 메서드
    public Tech(String tech, TechType type){
        this.tech_name = tech;
        this.type = type;
    }
}
