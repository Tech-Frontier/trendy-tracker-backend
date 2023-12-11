package com.trendyTracker.Job.service;

import com.trendyTracker.Job.domain.Company;
import com.trendyTracker.Job.domain.Model.CompanyInfo;
import com.trendyTracker.Job.domain.Recruit;
import com.trendyTracker.Job.domain.Tech;
import com.trendyTracker.Job.dto.JobInfoDto;
import com.trendyTracker.Job.repository.CompanyRepository;
import com.trendyTracker.Job.repository.JobRepository;
import com.trendyTracker.common.Exception.ExceptionDetail.NoResultException;
import com.trendyTracker.common.Exception.ExceptionDetail.NotAllowedValueException;
import com.trendyTracker.util.JobTotalCntSingleton;
import com.trendyTracker.util.TechUtils;
import com.trendyTracker.util.UrlReader;
import jakarta.validation.ValidationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
public class RecruitService {

  private final JobRepository jobRepository;
  private final CompanyRepository companyRepository;

  JobTotalCntSingleton jobTotalCntSingleton = JobTotalCntSingleton.getInstance();

  public long regisitJobPostion(String url, String company, String jobCategory)
    throws NoResultException, IOException {
    /*
     * url, company, jobCategory 를 활용해 채용공고 등록합니다
     */
    JobInfoDto jobInfo = UrlReader.getUrlContent(url);
    if (jobInfo.techSet().size() == 0) throw new NoResultException(
      "해당 url 에서 tech 가 발견되지 않았습니다"
    );

    CompanyInfo companyInfo = new CompanyInfo(null, null, company);
    Company newCompany = companyRepository.registCompany(companyInfo);

    // Singleton 데이터 변경
    jobTotalCntSingleton.increaseCnt();

    Recruit recruit = jobRepository.registJobPosition(
      url,
      jobInfo.title(),
      newCompany,
      jobCategory.toLowerCase(),
      new ArrayList<>(jobInfo.techSet())
    );

    return recruit.getId();
  }

  public Recruit getRecruitInfo(long recruit_id) {
    Recruit recruit = jobRepository
      .getRecruit(recruit_id)
      .orElseThrow(() -> new NotFoundException("공고 목록이 없습니다"));

    return recruit;
  }

  public Recruit getRecruitInfo(String url) {
    Recruit recruit = jobRepository
      .getRecruitByUrl(url)
      .orElseThrow(() -> new NotFoundException("공고 목록이 없습니다"));

    return recruit;
  }

  public Optional<Recruit> isRecruitExist(String url) {
    return jobRepository.getRecruitByUrl(url);
  }

  public long updateJobPosition(String url)
    throws IOException, NoResultException {
    /*
     * 기존 채용공고를 새로 Scrapping 합니다
     */
    var recruitInfo = getRecruitInfo(url);
    JobInfoDto jobInfo = UrlReader.getUrlContent(recruitInfo.getUrl());

    if (jobInfo.techSet().size() == 0) throw new NoResultException(
      "해당 url 에서 tech 가 발견되지 않았습니다"
    );

    Recruit result = jobRepository.updateJobPosition(
      recruitInfo.getId(),
      new ArrayList<>(jobInfo.techSet())
    );
    return result.getId();
  }

  public void deleteRecruit(long recruit_id) {
    /*
     * 채용공고 비활성화 합니다.
     */
    var recruitInfo = getRecruitInfo(recruit_id);
    // Singleton 데이터 변경
    jobTotalCntSingleton.decreaseCnt();

    jobRepository.deleteJobPosition(recruitInfo);
  }

  public Recruit updateRecruitTechs(long recruit_id, String[] techs)
    throws NotAllowedValueException {
    /*
     * 채용 공고의 기술 스택을 변경합니다.
     */
    Recruit recruitInfo = getRecruitInfo(recruit_id);
    if (TechUtils.isTechNotExist(techs)) throw new NotAllowedValueException(
      "존재하지 않는 기술이 존재합니다"
    );

    List<Tech> techList = TechUtils.makeTechs(techs);
    Optional<Recruit> result = jobRepository.updateRecruitTech(
      recruitInfo.getId(),
      techList
    );
    return result.get();
  }

  public List<Recruit> getRecruitList(
    String[] companies,
    String[] jobCategories,
    String[] techs,
    Integer pageNo,
    Integer pageSize
  ) throws NoResultException, ValidationException {
    /*
     * 채용 공고를 필터별로 조회합니다
     */
    // 페이징 파리미터 처리
    if ((pageNo == null && pageSize != null) || (pageNo != null && pageSize == null))     
      throw new ValidationException("pageNo, pageSize Error");

    // 필수 파라미터 처리
    if (companies[0].equals("*")) companies = null;

    // techs 소문자 처리
    companies = changeToLowerCase(companies);
    jobCategories = changeToLowerCase(jobCategories);
    techs = changeToLowerCase(techs);

    List<Recruit> recruitList = jobRepository.getRecruitList(
      companies,
      jobCategories,
      techs,pageNo, pageSize
    );
    if (recruitList.size() == 0) return new ArrayList<>();

    return recruitList;
  }

  private String[] changeToLowerCase(String[] paramStrings) {
    /*
     * Input params 를 소문자로 변경
     */
    if (paramStrings != null && paramStrings.length > 0) return Arrays
      .stream(paramStrings)
      .map(param -> param.toLowerCase())
      .toArray(String[]::new);

    return paramStrings;
  }


  public long getTotalJobCnt() {
    long totalJobCnt = jobRepository.getTotalJobCnt();

    // Singleton 데이터 삽입
    jobTotalCntSingleton.setTotalCnt(totalJobCnt);

    return totalJobCnt;
  }
}
