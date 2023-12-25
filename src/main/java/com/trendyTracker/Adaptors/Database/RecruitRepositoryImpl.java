package com.trendyTracker.Adaptors.Database;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.trendyTracker.Domain.Jobs.RecruitTechs.QRecruitTech;
import com.trendyTracker.Domain.Jobs.Recruits.QRecruit;
import com.trendyTracker.Domain.Jobs.Recruits.Recruit;
import com.trendyTracker.Domain.Jobs.Recruits.RecruitRepositoryCustom;

import jakarta.persistence.EntityManager;

@Repository
public class RecruitRepositoryImpl implements RecruitRepositoryCustom {
    
    private final EntityManager em;
    private JPAQueryFactory factory;

    public RecruitRepositoryImpl(EntityManager entityManager){
        this.em = entityManager;
        this.factory = new JPAQueryFactory(em);
    }

    @Override    
    public Recruit updateRecuitCategory(long id, String jobCategory) {
        Recruit recruit = em.find(Recruit.class, id);
        recruit.updateJobCategory(jobCategory);

        em.persist(recruit);        
        return recruit;
    }

    @Override
    public Optional<Recruit> findByUrl(String url) {
        QRecruit qRecruit = QRecruit.recruit;

        Recruit fetchFirst = factory.selectFrom(qRecruit)
            .where(qRecruit.url.eq(url))
            .where(qRecruit.isActive.isTrue())
            .fetchFirst();
        
        return Optional.ofNullable(fetchFirst);
    }

    @Override
    public List<Recruit> getActiveRecruits() {
        QRecruit qRecruit = QRecruit.recruit;

        return factory.selectFrom(qRecruit)
            .where(qRecruit.isActive.isTrue())
            .fetch();
    }

    @Override
    public List<Recruit> getRecruitList(String[] companies, String[] jobCategories, 
        String[] techs, Integer pageNo, Integer pageSize) {
    /*
     * 필터링 요소별 채용공고 필터링
     */
        QRecruit qRecruit = QRecruit.recruit;
        QRecruitTech qRecruitTech = QRecruitTech.recruitTech;

        JPAQuery<Recruit> query = factory.select(qRecruit)
            .from(qRecruit)
            .where(qRecruit.isActive.isTrue());
        
        if (techs != null && techs.length > 0)
            query.join(qRecruitTech).on(qRecruit.id.eq(qRecruitTech.recruit.id))
                .where(qRecruitTech.tech.techName.lower().in(techs));
        
        if (companies != null && companies.length > 0)
            query.where(qRecruit.company.companyName.lower().in(companies));

        if (jobCategories != null && jobCategories.length > 0)
            query.where(qRecruit.jobCategory.in(jobCategories));

        if (pageNo != null && pageSize != null)
            query.offset((long) pageNo * pageSize).limit(pageSize);

        return query.fetch();
    }
    
}
