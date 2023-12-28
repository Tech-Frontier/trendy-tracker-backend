package com.trendyTracker.Adaptors.Database;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.trendyTracker.Domain.Jobs.RecruitTechs.QRecruitTech;
import com.trendyTracker.Domain.Jobs.Recruits.QRecruit;
import com.trendyTracker.Domain.Jobs.Statistics.StaticsRepository;
import com.trendyTracker.Domain.Jobs.Statistics.Dto.ChartInfo;

import jakarta.persistence.EntityManager;

@Repository
public class StaticsRepositoryImpl implements StaticsRepository {
    private final EntityManager em;
    private JPAQueryFactory factory;

    public StaticsRepositoryImpl(EntityManager entityManager){
        this.em = entityManager;
        this.factory = new JPAQueryFactory(em);
    }

    @Override
    public ChartInfo getTechChart(LocalDate fromDate, LocalDate toDate) {
    /*
     * 기간별 Tech 사용 건수 추출
     */
        QRecruit qRecruit = QRecruit.recruit;
        QRecruitTech qRecruitTech = QRecruitTech.recruitTech;
        HashMap<String, Integer> chartMap = new HashMap<>();

        List<Tuple> results = factory
            .select(qRecruitTech.tech.techName, qRecruitTech.count())
            .from(qRecruit)
            .innerJoin(qRecruitTech).on(qRecruit.id.eq(qRecruitTech.recruit.id))
            .where(qRecruit.createAt.between(fromDate.atStartOfDay(),toDate.atStartOfDay()))
            .groupBy(qRecruitTech.tech.techName)
            .fetch();
        
        for(Tuple result : results){
            String techName = result.get(qRecruitTech.tech.techName);
            Integer count = result.get(qRecruit.count()).intValue();
            chartMap.put(techName,count);
        }

        return new ChartInfo(fromDate, toDate, chartMap);
    }
}
