package com.trendyTracker.Job.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.trendyTracker.Job.domain.Company;
import com.trendyTracker.Job.domain.QCompany;
import com.trendyTracker.Job.domain.QRecruit;
import com.trendyTracker.Job.domain.Recruit;
import com.trendyTracker.Job.domain.RecruitTech;
import com.trendyTracker.Job.domain.Tech;
import com.trendyTracker.Job.dto.RecruitDto;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JobRepositoryImpl implements JobRepository {
    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    //#region [CREATE]
    @Override
    @Transactional
    public Company registeCompany(String company) {
    /*
     * 'Company' 저장
     */
        queryFactory = new JPAQueryFactory(em);
        QCompany qCompany = QCompany.company;
        Company newCompany = new Company();
        
        var findCompany = queryFactory.select(qCompany)
                .from(qCompany)
                .where(qCompany.company_name.eq(company))
                .fetchOne();

        if (findCompany != null)
            return findCompany;

        newCompany.addCompany(company);
        em.persist(newCompany);
        return newCompany;
    }

    @Override
    @Transactional
    public Recruit registJobPosition(String url, String title, Company company, String jobPosition, List<Tech> techList) {
    /*
     * 'Recruit',  'RecruitTech'  저장
     */
        List<RecruitTech> recruitTechList = new ArrayList<>();
        Recruit recruit = new Recruit();
        recruit.addRecruit(url, title, company, jobPosition);

        for (Tech newTech : techList) {
            RecruitTech recruitTech = new RecruitTech();
            recruitTech.addRecruitTech(recruit, newTech);
            recruitTechList.add(recruitTech);
        }
        
        recruit.setUrlTechs(recruitTechList);
        em.persist(recruit);
        return recruit;
    }
    //#endregion

    //#region [UPDATE]
    @Override
    @Transactional
    public void deleteJobPosition(Recruit recruit) {
    /*
     * 'Recruit' 비활성화
     */
        recruit.deleteRecruit();
        em.persist(recruit);
    }

    @Override
    @Transactional
    public Optional<Recruit> updateRecruitTech(long recruit_id, List<Tech> techList) {
    /*
     * 'RecruitTech' 변경
     */
        Recruit recruit = em.find(Recruit.class, recruit_id);
        List<RecruitTech> urlTechs = recruit.getUrlTechs();
        for (RecruitTech recruitTech : urlTechs) {
            em.remove(recruitTech);
        }

        recruit.updateUrlTechs(techList);
        em.persist(recruit);
        
        return Optional.of(recruit);
    }
    //#endregion

    //#region [READ]
    @Override
    public Optional<Recruit> getRecruit(long recruit_id) {
    /*
     * recruit_id 로 채용공고 조회
     */
        Recruit recruit = em.find(Recruit.class, recruit_id);
        if(recruit == null || !recruit.getIs_active()) 
            return Optional.empty();

        return Optional.of(recruit);    
    }

    @Override
    public List<Recruit> getRecruitList(String[] companies, String[] jobCategories, String[] techs) {
    /*
     * 'companies', 'jobCategories', 'techs' 별 채용공고 필터링
     */
        queryFactory = new JPAQueryFactory(em);
        QRecruit qRecruit = QRecruit.recruit;

        JPAQuery<Long> query = queryFactory.select(qRecruit.id)
                        .from(qRecruit).where(qRecruit.is_active.eq(true));
        
        if (companies != null && companies.length >0)
            query.where(qRecruit.company.company_name.in(companies));
        
        if (jobCategories != null && jobCategories.length >0)
            query.where(qRecruit.jobCategory.in(jobCategories));
        
        return filteringTechs(techs, query.fetch());
    }

    private List<Recruit> filteringTechs(String[] techs, List<Long> recruitIdList) {
    /**
     * 사용자가 지정한 tech 필터링
     */
        List<Recruit> recruitList = new ArrayList<Recruit>();
        List<String> techList = techs != null ? Arrays.asList(techs) : new ArrayList<>();

        for(int i=0; i< recruitIdList.size(); i++){
            Recruit recruit = em.find(Recruit.class, recruitIdList.get(i));
            List<RecruitTech> urlTechs = recruit.getUrlTechs();
            
            if (techList.size() ==0)
                recruitList.add(recruit);
            
            else 
                if(urlTechs.stream()
                    .anyMatch(t -> techList.stream()
                    .anyMatch(tech -> tech.equalsIgnoreCase(t.getTech().getTech_name()))))
                    recruitList.add(recruit);
        }
        return recruitList;
    }

    @Override
    public long getTotalJobCnt() {
        queryFactory = new JPAQueryFactory(em);
        QRecruit qRecruit = QRecruit.recruit;

        var result = queryFactory.select(qRecruit.count())
                                    .from(qRecruit)
                                    .where(qRecruit.is_active.eq(true))
                                    .fetchOne();
        return result;
    }

    //#endregion
}
