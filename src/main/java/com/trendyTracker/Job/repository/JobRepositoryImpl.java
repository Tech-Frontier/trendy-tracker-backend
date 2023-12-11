package com.trendyTracker.Job.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.trendyTracker.Job.domain.Company;
import com.trendyTracker.Job.domain.QRecruit;
import com.trendyTracker.Job.domain.QRecruitTech;
import com.trendyTracker.Job.domain.Recruit;
import com.trendyTracker.Job.domain.RecruitTech;
import com.trendyTracker.Job.domain.Tech;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JobRepositoryImpl implements JobRepository {

  private final EntityManager em;
  private JPAQueryFactory queryFactory;

  //#region [CREATE]
  @Override
  @Transactional
  public Recruit registJobPosition(
    String url,
    String title,
    Company company,
    String jobPosition,
    List<Tech> techList
  ) {
    /*
     * 'Recruit',  'RecruitTech'  저장
     */
    List<RecruitTech> recruitTechList = new ArrayList<>();
    Recruit recruit = new Recruit(url, title, company, jobPosition);

    for (Tech newTech : techList) {
      RecruitTech recruitTech = new RecruitTech(recruit, newTech);
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
  public Recruit updateJobPosition(long id, List<Tech> techList) {
    /*
     * 채용공고 재분석
     */
    Recruit recruit = em.find(Recruit.class, id);
    recruit.updateUrlTechs(techList);
    em.persist(recruit);
    return recruit;
  }

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
  public Optional<Recruit> updateRecruitTech(
    long recruit_id,
    List<Tech> techList
  ) {
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
    if (recruit == null || !recruit.getIs_active()) return Optional.empty();

    return Optional.of(recruit);
  }

  public Optional<Recruit> getRecruitByUrl(String url) {
    /*
     * 채용공고 Url 로 채용공고 조회
     */
    queryFactory = new JPAQueryFactory(em);
    QRecruit qRecruit = QRecruit.recruit;

    Recruit recruit = queryFactory
      .select(qRecruit)
      .from(qRecruit)
      .where(qRecruit.url.eq(url))
      .fetchFirst();

    if (recruit == null || !recruit.getIs_active()) return Optional.empty();

    return Optional.of(recruit);
  }

  @Override
  public List<Recruit> getRecruitList(
    String[] companies, String[] jobCategories,String[] techNames,
    Integer pageNo, Integer pageSize
  ) {
    /*
     * 'companies', 'jobCategories', 'techs' 별 채용공고 필터링
     */

    queryFactory = new JPAQueryFactory(em);
    QRecruit qRecruit = QRecruit.recruit;
    QRecruitTech qRecruitTech = QRecruitTech.recruitTech;


    JPAQuery<Recruit> query = queryFactory
        .select(qRecruit)
        .from(qRecruit)
        .where(qRecruit.is_active.isTrue());

    if (techNames != null && techNames.length > 0) {
      query.join(qRecruitTech).on(qRecruit.id.eq(qRecruitTech.recruit.id))
          .where(qRecruitTech.tech.tech_name.lower().in(techNames));
    }    
    if (companies != null && companies.length > 0) 
      query.where(qRecruit.company.company_name.in(companies));

    if (jobCategories != null && jobCategories.length > 0) 
      query.where(qRecruit.jobCategory.in(jobCategories));

    if (pageNo != null && pageSize != null)
      query.offset((long) pageNo * pageSize).limit(pageSize);

    return query.fetch();
  }

  @Override
  public long getTotalJobCnt() {
    queryFactory = new JPAQueryFactory(em);
    QRecruit qRecruit = QRecruit.recruit;

    var result = queryFactory
      .select(qRecruit.count())
      .from(qRecruit)
      .where(qRecruit.is_active.eq(true))
      .fetchOne();
    return result;
  }
  //#endregion
}
