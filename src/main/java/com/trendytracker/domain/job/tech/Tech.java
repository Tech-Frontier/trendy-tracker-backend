package com.trendytracker.domain.job.tech;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trendytracker.domain.job.recruittech.RecruitTech;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name ="tech", schema = "tech_frontier")
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
    
    @OneToMany(
        mappedBy = "tech",
        fetch = FetchType.LAZY        
    )
    @JsonIgnore
    private List<RecruitTech> recruitTechs = new ArrayList<>();

    public Tech(String tech, TechType type){
        this.techName = tech;
        this.type = type;
    }
}
