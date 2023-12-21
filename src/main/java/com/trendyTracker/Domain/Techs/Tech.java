package com.trendyTracker.Domain.Techs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name ="tech", schema = "public")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tech {
    
    public enum TechType {
        LANGUAGE,
        DATABASE,
        FRAMEWORK,
        LIBRARIES,
        TOOLS,
        IDE,
        OTHER,
      }
    
    @Id
    @Column(name ="tech_name", unique = true)
    private String techName;

    @Enumerated(EnumType.STRING)
    private TechType type;

    public Tech(String tech, TechType type){
        this.techName = tech;
        this.type = type;
    }
}
