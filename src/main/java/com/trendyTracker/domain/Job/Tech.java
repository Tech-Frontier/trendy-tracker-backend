package com.trendyTracker.domain.Job;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Id
    @Column(name="tech_name", unique = true)
    private String tech_name;

    public Tech(String tech){
        this.tech_name = tech;
    }
}
