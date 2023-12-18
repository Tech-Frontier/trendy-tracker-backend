package com.trendyTracker.Job.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.trendyTracker.Job.domain.QRecruit;
import com.trendyTracker.Job.domain.QRecruitTech;
import com.trendyTracker.Job.domain.Model.ChartInfo;

import jakarta.persistence.EntityManager;

@Repository
public class ChartRepositoryImpl  implements ChartRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ChartRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public ChartInfo getTechChart(LocalDate fromDate, LocalDate toDate) {
        /*
         * 기간별 Tech 사용 건수 추출 
         */

        QRecruit qRecruit = QRecruit.recruit;
        QRecruitTech qRecruitTech = QRecruitTech.recruitTech;
        HashMap<String, Integer> chartMap = new HashMap<>();

        List<Tuple> results = queryFactory.select(qRecruitTech.tech.tech_name, qRecruitTech.count())
                    .from(qRecruit)
                    .innerJoin(qRecruitTech).on(qRecruit.id.eq(qRecruitTech.recruit.id))
                    .where(qRecruit.create_time.between(fromDate.atStartOfDay(), toDate.atStartOfDay()))
                    .groupBy(qRecruitTech.tech.tech_name)
                    .fetch();
    
        for(Tuple result : results){
            String techName = result.get(qRecruitTech.tech.tech_name);
            Integer count = result.get(qRecruitTech.count()).intValue();
            chartMap.put(techName, count);
        }

        return new ChartInfo(fromDate,toDate,chartMap);
    }
}
