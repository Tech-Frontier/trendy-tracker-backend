package com.trendyTracker.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.trendyTracker.Dto.Recruit.RecruitDto;
import com.trendyTracker.domain.AppService.QCompany;
import com.trendyTracker.domain.Job.Company;
import com.trendyTracker.domain.Job.QRecruit;
import com.trendyTracker.domain.Job.Recruit;
import com.trendyTracker.domain.Job.RecruitTech;
import com.trendyTracker.domain.Job.Tech;

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
    public long registJobPosition(String url, Company company, String jobPosition, List<String> techList) {
        List<RecruitTech> recruitTechList = new ArrayList<>();
        Recruit recruit = new Recruit();
        recruit.addRecruit(url, company, jobPosition);

        for (String newTech : techList) {
            RecruitTech recruitTech = new RecruitTech();
            recruitTech.addRecruitTech(recruit, new Tech(newTech));
            recruitTechList.add(recruitTech);
        }
        
        recruit.setUrlTechs(recruitTechList);
        em.persist(recruit);
        return recruit.getId();
    }
    //#endregion

    //#region [READ]
    @Override
    public Optional<RecruitDto> getRecruit(long recruit_id) {
        Recruit recruit = em.find(Recruit.class, recruit_id);

        List<String> techList = recruit.getUrlTechs()
            .stream().map(recruitTech -> recruitTech.getTech().getTech_name())
            .collect(Collectors.toList());

        RecruitDto result = new RecruitDto(recruit_id, recruit.getCompany(), 
                                            recruit.getOccupation(),recruit.getUrl(), 
                                            recruit.getCreate_time());
        result.setTechList(techList);
        return Optional.of(result);    
    }

    @Override
    public List<RecruitDto> getRecruitList() {
        queryFactory = new JPAQueryFactory(em);
        QRecruit qRecruit = QRecruit.recruit;

        List<RecruitDto> recruitDtoList = queryFactory.select(Projections.constructor(RecruitDto.class, 
                        qRecruit.id, qRecruit.company, qRecruit.occupation,
                        qRecruit.url, qRecruit.create_time))
                    .from(qRecruit)
                    .fetch();

        for (int i =0; i < recruitDtoList.size(); i++) {
            List<String> techList = em.find(Recruit.class ,recruitDtoList.get(i).getId()).getUrlTechs()
                .stream().map(recruitTech -> recruitTech.getTech().getTech_name())
                .collect(Collectors.toList());
                
            recruitDtoList.get(i).setTechList(techList);
        }
        return recruitDtoList;
    }

    public List<RecruitDto> getRecruitListByCompany(String company) {
        queryFactory = new JPAQueryFactory(em);
        QRecruit qRecruit = QRecruit.recruit;

        List<RecruitDto> recruitDtoList = queryFactory.select(Projections.constructor(RecruitDto.class, 
                        qRecruit.id, qRecruit.company, qRecruit.occupation,
                        qRecruit.url, qRecruit.create_time))
                    .from(qRecruit).where(qRecruit.company.company_name.eq(company))
                    .fetch();

        for (int i =0; i < recruitDtoList.size(); i++) {
            List<String> techList = em.find(Recruit.class ,recruitDtoList.get(i).getId()).getUrlTechs()
                .stream().map(recruitTech -> recruitTech.getTech().getTech_name())
                .collect(Collectors.toList());
                
            recruitDtoList.get(i).setTechList(techList);
        }
        return recruitDtoList;
    }
    //#endregion
}
