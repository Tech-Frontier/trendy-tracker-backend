package com.trendyTracker.domain.Job;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name ="recruit", schema = "public"
    , indexes = @Index(name = "company", columnList = "company, create_time"))
public class Recruit {
    @Id
    @GeneratedValue
    @Column(name ="recruit_id")
    private long id;

    @OneToMany(mappedBy = "recruit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UrlTech> urlTechs;
    
    private String company;

    private String occupation;

    private String url;

    private LocalDateTime create_time;

    private LocalDateTime updated_time;

    private Boolean is_active;

}
