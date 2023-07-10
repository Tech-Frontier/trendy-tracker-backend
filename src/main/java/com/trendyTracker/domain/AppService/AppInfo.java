package com.trendyTracker.domain.AppService;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "app_info", schema = "public")
public class AppInfo {

    @Id
    @GeneratedValue
    private Long id;

    private String version;

    @Column(columnDefinition = "TEXT")
    private String terms;

    // 연관관계 메서드
    public void addAppInfo(String version, String terms) {
        this.version = version;
        this.terms = terms;
    }

}
