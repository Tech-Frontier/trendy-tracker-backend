package com.trendyTracker.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.trendyTracker.domain.Job.QTech;
import com.trendyTracker.domain.Job.Tech;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TechRepositoryImpl implements TechRepository {
    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    @Override
    @Transactional
    public void registTechStack(String tech_name) {
        Tech tech = new Tech(tech_name);
        em.persist(tech);
    }

    @Override
    public Boolean isTechRegist(String tech_name) {
        queryFactory = new JPAQueryFactory(em);

        QTech qTech = QTech.tech;
        Tech fetchFirst = queryFactory.selectFrom(qTech)
                                .where(qTech.tech_name.eq(tech_name))
                                .fetchFirst();

        if (fetchFirst == null)
            return false;
        
        return true;
    }

    @Override
    @Transactional
    public void deleteTechStack(String tech_name) {
        Tech tech = em.find(Tech.class, tech_name);
        if (tech != null)
            em.remove(tech);
    }

    @Override
    public List<Tech> getTechList() {
        queryFactory = new JPAQueryFactory(em);

        QTech qTech = QTech.tech;
        List<Tech> techList = queryFactory.select(qTech).from(qTech).fetch();

        return techList;
    }

}
