package com.trendyTracker.api.Recruit;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trendyTracker.Job.domain.Recruit;
import com.trendyTracker.Job.dto.RecruitDto;
import com.trendyTracker.Job.service.RecruitService;
import com.trendyTracker.common.Exception.ExceptionDetail.AlreadyExistException;
import com.trendyTracker.common.Exception.ExceptionDetail.NoResultException;
import com.trendyTracker.common.Exception.ExceptionDetail.NotAllowedValueException;
import com.trendyTracker.common.config.logging.Loggable;
import com.trendyTracker.common.response.Response;
import com.trendyTracker.infrastructure.kafka.KafkaProducer;
import com.trendyTracker.util.JobTotalCntSingleton;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.ValidationException;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Loggable
@Tag(name = "Recruit", description = "채용 공고")
@RequestMapping("/api/recruit")
@RequiredArgsConstructor
@RestController
public class RecruitController {
    @Autowired
    private KafkaProducer kafkaProducer;
    private final RecruitService recruitService; 

    @Operation(summary = "채용 공고 등록")
    @PostMapping(value = "/regist")
    public Response<Void> regisitJobPostion(
        @RequestBody @Validated recruitRegistRequest recruitRequest,
        HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException, AlreadyExistException {
        Optional<Recruit> recruitExist = recruitService.isRecruitExist(recruitRequest.url);
        
        if(recruitExist.isPresent())
            throw new AlreadyExistException("해당 공고가 존재합니다");

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("url", recruitRequest.url);
        paramMap.put("jobCategory", recruitRequest.jobCategory);
        paramMap.put("company", recruitRequest.company);

        String uuid = addHeader(request, response);
        kafkaProducer.sendMessage("RegistJob", paramMap, uuid);    

        return Response.success(200, "채용 공고 등록이 대기열에 저장 되었습니다");
    }

    @Operation(summary = "채용 공고 수정")
    @PutMapping(value = "update")
    public Response<Void> updateRecruit(
        @RequestBody @Validated recruitUpdateRequest recruitRequest,
        HttpServletRequest request, HttpServletResponse response) throws NotAllowedValueException, JsonProcessingException {
        
        recruitService.getRecruitInfo(recruitRequest.url);

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("url", recruitRequest.url);
        
        String uuid = addHeader(request, response);
        kafkaProducer.sendMessage("UpdateJob", paramMap, uuid);    
        
        return Response.success(200, "채용 공고 수정이 대기열에 저장 되었습니다");
        
    }

    @Operation(summary = "채용 공고 조회")
    @GetMapping(value = "/id/{recruit_id}")
    public Response<RecruitDto> getRecruitDetail(
        @PathVariable(name = "recruit_id") Long recruit_id,
        HttpServletRequest request, HttpServletResponse response) {

        Recruit recruit = recruitService.getRecruitInfo(recruit_id);
        RecruitDto recruitDto = new RecruitDto(
            recruit.getId(),
            recruit.getCompany(), 
            recruit.getJobCategory(),
            recruit.getUrl(),
            recruit.getTitle(),
            recruit.getCreate_time(),
            recruit.getTechList());


        addHeader(request, response);
        return Response.success(200, "공고 목록이 조회되었습니다", recruitDto);
    }

    @Operation(summary = "채용 공고 삭제")
    @DeleteMapping(value = "delete/id/{recruit_id}")
    public Response<Void> deleteRecruitDetail(
        @PathVariable(name = "recruit_id") Long recruit_id,
        HttpServletRequest request, HttpServletResponse response) {

        recruitService.deleteRecruit(recruit_id);

        addHeader(request, response);
        return Response.success(200, "공고 목록이 삭제되었습니다.");
    }

    @Operation(summary = "채용 공고 기술 수정")
    @PutMapping(value = "update/id/{recruit_id}")
    public Response<RecruitDto> updateRecruit(
        @PathVariable(name = "recruit_id") Long recruit_id,
        @RequestParam(name ="tech",required = true) String[] techs,
        HttpServletRequest request, HttpServletResponse response) throws NotAllowedValueException {
        
        Recruit recruit = recruitService.updateRecruitTechs(recruit_id,techs);
        RecruitDto recruitDto = new RecruitDto(
        recruit.getId(),
        recruit.getCompany(), 
        recruit.getJobCategory(),
        recruit.getUrl(),
        recruit.getTitle(),
        recruit.getCreate_time(),
        recruit.getTechList()
        );

        addHeader(request, response);
        return Response.success(200, "공고 스택이 변경되었습니다.",recruitDto);
    }
// for (Recruit recruit : recruitList) {
        //     recruitDtoList.add( new RecruitDto(
        //         recruit.getId(),
        //         recruit.getCompany(), 
        //         recruit.getJobCategory(),
        //         recruit.getUrl(),
        //         recruit.getTitle(),
        //         recruit.getCreate_time(),
        //         recruit.getTechList()));
        // }

    @Operation(summary = "전체 채용 공고 조회")
    @GetMapping(value = "/list")
    public Response<HashMap<String,Object>> getRecruits(
        @RequestParam(name ="company", required = false, defaultValue = "*") String[] companies,
        @RequestParam(name ="jobCategory",required = false) String[] jobCategories,
        @RequestParam(name ="tech",required = false) String[] techs,
        @RequestParam(name ="pageNo" ,required = false) Integer pageNo,
        @RequestParam(name= "pageSize",required = false) Integer pageSize,
        HttpServletRequest request, HttpServletResponse response) throws NoResultException, ValidationException {
        
        HashMap<String,Object> result = new HashMap<>();
        List<Recruit> recruitList = recruitService.getRecruitList(companies,jobCategories,techs,pageNo,pageSize);    
                        
        List<RecruitDto> recruitDtoList = recruitList.stream().map(recruit -> {      
        return new RecruitDto(
            recruit.getId(),
            recruit.getCompany(),
            recruit.getJobCategory(),
            recruit.getUrl(),
            recruit.getTitle(),
            recruit.getCreate_time(),
            recruit.getTechList()
        );
        }).collect(Collectors.toList());
        
        // Singleton 데이터 조회
        JobTotalCntSingleton jobTotalCntSingleton = JobTotalCntSingleton.getInstance();
        result.put("recruitList", recruitDtoList);
        result.put("totalCount", jobTotalCntSingleton.getTotalCnt());
        result.put("filterCount", recruitDtoList.size());
        
        addHeader(request, response);

        return Response.success(200, "공고 목록이 조회되었습니다", result);
    }

    private String addHeader(HttpServletRequest request, HttpServletResponse response) {
        UUID uuid = (UUID) request.getAttribute("uuid");
        response.addHeader("uuid", uuid.toString());
        return uuid.toString();
    }

    @Data
    static class recruitRegistRequest {
        @NotNull
        @Schema(description = "Url", example = "https://toss.im/career/job-detail?job_id=4071141003&company=%ED%86%A0%EC%8A%A4%EB%B1%85%ED%81%AC&gh_src=a6133a833us&utm_source=offline_conference&utm_medium=banner&utm_campaign=2307_tossbank_recruit", type = "String")
        private String url;

        @Schema(description = "회사명", example = "toss bank", type = "String")
        private String company;

        @Schema(description = "직군", example = "Backend", type = "String")
        private String jobCategory;
    }

    @Data
    static class recruitUpdateRequest {
        @NotNull
        @Schema(description = "Url", example = "https://toss.im/career/job-detail?job_id=4071141003&company=%ED%86%A0%EC%8A%A4%EB%B1%85%ED%81%AC&gh_src=a6133a833us&utm_source=offline_conference&utm_medium=banner&utm_campaign=2307_tossbank_recruit", type = "String")
        private String url;
    }
}
