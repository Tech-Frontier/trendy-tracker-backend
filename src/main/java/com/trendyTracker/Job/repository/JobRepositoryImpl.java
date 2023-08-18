package com.trendyTracker.Job.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.trendyTracker.Job.domain.Company;
import com.trendyTracker.Job.domain.QCompany;
import com.trendyTracker.Job.domain.QRecruit;
import com.trendyTracker.Job.domain.Recruit;
import com.trendyTracker.Job.domain.RecruitTech;
import com.trendyTracker.Job.domain.Tech;
import com.trendyTracker.Job.dto.RecruitDto;
import com.trendyTracker.util.TechUtils;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
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
    public long registJobPosition(String url, Company company, String jobPosition, List<Tech> techList) {
    /*
     * 'Recruit',  'RecruitTech'  저장
     */
        List<RecruitTech> recruitTechList = new ArrayList<>();
        Recruit recruit = new Recruit();
        recruit.addRecruit(url, company, jobPosition);

        for (Tech newTech : techList) {
            RecruitTech recruitTech = new RecruitTech();
            recruitTech.addRecruitTech(recruit, newTech);
            recruitTechList.add(recruitTech);
        }
        
        recruit.setUrlTechs(recruitTechList);
        em.persist(recruit);
        return recruit.getId();
    }
    //#endregion

    //#region [UPDATE]
    @Override
    @Transactional
    public void deleteJobPosition(long recruit_id) {
    /*
     * 'Recruit' 비활성화
     */
        Recruit recruit = em.find(Recruit.class, recruit_id);
        recruit.setIs_active(false);

        em.persist(recruit);
    }

    @Override
    @Transactional
    public Optional<RecruitDto> updateRecruitTech(long recruit_id, List<Tech> techList) {
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
        
        RecruitDto result = new RecruitDto(recruit_id, recruit.getCompany(), 
                                    recruit.getJobCategory(),recruit.getUrl(), 
                                    recruit.getCreate_time(),
                                    TechUtils.getTechNameList(techList));
        return Optional.of(result);
    }
    //#endregion

    //#region [READ]
    @Override
    public Optional<RecruitDto> getRecruit(long recruit_id) {
    /*
     * recruit_id 로 채용공고 조회
     */
        Recruit recruit = em.find(Recruit.class, recruit_id);
        if(!recruit.getIs_active()) 
            return Optional.empty();

        List<String> techList = recruit.getUrlTechs()
            .stream().map(recruitTech -> recruitTech.getTech().getTech_name())
            .collect(Collectors.toList());

        RecruitDto result = new RecruitDto(recruit_id, recruit.getCompany(), 
                                            recruit.getJobCategory(),recruit.getUrl(), 
                                            recruit.getCreate_time());
        result.setTechList(techList);
        return Optional.of(result);    
    }

    @Override
    public List<RecruitDto> getRecruitList(String[] companies, String[] jobCategories, String[] techs) {
    /*
     * 'companies', 'jobCategories', 'techs' 별 채용공고 필터링
     */
        queryFactory = new JPAQueryFactory(em);
        QRecruit qRecruit = QRecruit.recruit;

        JPAQuery<RecruitDto> query = queryFactory.select(Projections.constructor(RecruitDto.class, 
                        qRecruit.id, qRecruit.company, qRecruit.jobCategory,
                        qRecruit.url, qRecruit.create_time))
                    .from(qRecruit).where(qRecruit.is_active.eq(true));
        
        if (companies != null && companies.length >0)
            query.where(qRecruit.company.company_name.in(companies));
        
        if (jobCategories != null && jobCategories.length >0)
            query.where(qRecruit.jobCategory.in(jobCategories));

        List<RecruitDto> recruitDtoList = filteringTechs(techs, query.fetch());
        return recruitDtoList;
    }

    private List<RecruitDto> filteringTechs(String[] techs, List<RecruitDto> recruitDtoList) {
    /**
     * 사용자가 지정한 tech 가 있으면 필터링
     */
        List<Integer> indexList = new ArrayList<>();
        List<String> techList = new ArrayList<>();

        if (techs != null && techs.length >0) 
            techList = Arrays.asList(techs);

        for (int i =0; i < recruitDtoList.size(); i++) {
            List<String> recruitTechList = em.find(Recruit.class ,recruitDtoList.get(i).getId())
                .getUrlTechs()
                .stream().map(recruitTech -> recruitTech.getTech().getTech_name())
                .map(String::toLowerCase)
                .collect(Collectors.toList());
            
            // 사용자 지정 tech 없는 index 추출 
            if (techList.size() >0) 
                if(recruitTechList.stream().noneMatch(techList::contains)) 
                    indexList.add(i);

            recruitDtoList.get(i).setTechList(recruitTechList);
        }
        // 실제 필터링
        Collections.sort(indexList, Collections.reverseOrder());
        for (int integer : indexList) 
            recruitDtoList.remove(integer);
        
        return recruitDtoList;
    }

    //#endregion
}
