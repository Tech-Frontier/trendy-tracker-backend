package com.trendyTracker.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.trendyTracker.domain.AppService.AppInfo;
import com.trendyTracker.domain.AppService.QAppInfo;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AppInfoRepositoryImpl implements AppInfoRepository {

    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    @Override
    public Optional<AppInfo> getAppInfo() {
        queryFactory = new JPAQueryFactory(em);

        QAppInfo qAppInfo = QAppInfo.appInfo;
        var result = queryFactory.selectFrom(qAppInfo)
                .orderBy(qAppInfo.version.desc())
                .fetchFirst();

        return Optional.ofNullable(result);
    }

    @Override
    public void saveAppInfo(String version, String terms) {
        AppInfo newAppInfo = new AppInfo();
        newAppInfo.addAppInfo(version, terms);

        em.persist(newAppInfo);
    }

}
