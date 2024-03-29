package com.trendytracker.adaptors.database;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.trendytracker.domain.job.recruit.QRecruit;
import com.trendytracker.domain.job.recruittech.QRecruitTech;
import com.trendytracker.domain.job.statistic.StaticsRepository;
import com.trendytracker.domain.job.statistic.dto.ChartInfo;
import com.trendytracker.domain.job.statistic.dto.TrendInfo;
import com.trendytracker.domain.job.tech.QTech;
import com.trendytracker.domain.job.tech.Tech;
import com.trendytracker.domain.job.tech.Tech.TechType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
public class StaticsRepositoryImpl implements StaticsRepository {
    private final EntityManager em;
    private JPAQueryFactory factory;

    public StaticsRepositoryImpl(EntityManager entityManager){
        this.em = entityManager;
        this.factory = new JPAQueryFactory(em);
    }

    @Override
    public ChartInfo getTechChart(LocalDate fromDate, LocalDate toDate, TechType techType) {
    /*
     * 기간별 Tech 사용 건수 추출
     */
        QRecruit qRecruit = QRecruit.recruit;
        QRecruitTech qRecruitTech = QRecruitTech.recruitTech;
        QTech qTech = QTech.tech;
        HashMap<String, Long> chartMap = new HashMap<>();

        List<Tuple> results = factory
            .select(qRecruitTech.tech.techName, qRecruitTech.count())
            .from(qRecruit)
            .innerJoin(qRecruitTech).on(qRecruit.id.eq(qRecruitTech.recruit.id))
            .innerJoin(qTech).on(qRecruitTech.tech.techName.eq(qTech.techName))
            .where(qRecruit.createAt.between(fromDate.atStartOfDay(), toDate.atStartOfDay()))
            .where(qTech.type.eq(techType))
            .groupBy(qRecruitTech.tech.techName)
            .orderBy(qRecruitTech.count().desc())
            .fetch();
        
        for(Tuple result : results){
            String techName = result.get(qRecruitTech.tech.techName);
            Long count = result.get(qRecruitTech.count());
            chartMap.put(techName,count);
        }

        return new ChartInfo(fromDate, toDate, chartMap);
    }

    @Override
    public List<TrendInfo> getTrendInfo(Tech tech) {
    /*
     * 월별 Tech 채용공고 및 퍼센트
     */
        
        String sql ="""
            WITH standard AS (
                SELECT EXTRACT (YEAR FROM r.create_time) AS YEAR, 
                        EXTRACT (MONTH FROM r.create_time) AS MONTH,
                        count(*) AS count
                FROM recruit r 
                WHERE r.recruit_id IN (
                    SELECT DISTINCT rt.recruit_id 
                    FROM  recruit_tech rt INNER JOIN tech t 
                    ON rt.tech_name = t.tech_name
                    WHERE t."type" = :techType
                ) 
                GROUP BY EXTRACT (YEAR FROM r.create_time),
                        EXTRACT (MONTH FROM r.create_time)
                ORDER BY EXTRACT (YEAR FROM r.create_time) DESC,
                        EXTRACT (MONTH FROM r.create_time) DESC 
                ),
            target AS (	
                SELECT EXTRACT (YEAR FROM r.create_time) AS YEAR, 
                        EXTRACT (MONTH FROM r.create_time) AS MONTH,
                        count(*) AS count
                FROM recruit r 
                WHERE r.recruit_id IN (
                    SELECT DISTINCT rt.recruit_id 
                    FROM  recruit_tech rt INNER JOIN tech t 
                    ON rt.tech_name = t.tech_name 
                    WHERE t."type" = :techType
                    AND t.tech_name = :techName
                ) 
                GROUP BY EXTRACT (YEAR FROM r.create_time),
                        EXTRACT (MONTH FROM r.create_time)
                ORDER BY EXTRACT (YEAR FROM r.create_time) DESC,
                        EXTRACT (MONTH FROM r.create_time) DESC 
            )
            SELECT  TO_DATE(t.YEAR || '-' ||  t.MONTH , 'YYYY-MM') AS datetime,
                    t.count,
                    ROUND(((CAST(t.count AS float) / CAST(s.count AS float) ) * 100)) || '%'  AS percent
            FROM target t INNER JOIN standard s 
            ON (t.YEAR = s.YEAR AND t.MONTH = s.MONTH)
            ;
            """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("techName", tech.getTechName());
        query.setParameter("techType", tech.getType().name());
        
        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.getResultList();
        List<TrendInfo> results = resultList.stream()
            .map(result -> new TrendInfo(
                ((java.sql.Date) result[0]).toLocalDate(), 
                (Long) result[1],
                (String) result[2]  
            ))
            .collect(Collectors.toList());

        return results;
    }
}
