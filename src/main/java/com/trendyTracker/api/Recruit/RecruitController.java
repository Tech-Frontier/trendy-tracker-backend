package com.trendyTracker.api.Recruit;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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

import com.trendyTracker.Job.dto.RecruitDto;
import com.trendyTracker.Job.service.RecruitService;
import com.trendyTracker.common.Exception.ExceptionDetail.NoResultException;
import com.trendyTracker.common.Exception.ExceptionDetail.NotAllowedValueException;
import com.trendyTracker.common.config.Loggable;
import com.trendyTracker.common.response.Response;
import com.trendyTracker.util.JobTotalCntSingleton;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.tags.Tag;
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

    private final RecruitService recruitService;

    @Operation(summary = "채용 공고 등록")
    @PostMapping(value = "/regist")
    public Response<Long> regisitJobPostion(
        @RequestBody @Validated recruitRequest request) throws NoResultException, IOException {

        long id = recruitService.regisitJobPostion(request.url, request.company, request.occupation);
        return Response.success(200, "공고 목록이 조회되었습니다", id);
    }

    @Operation(summary = "채용 공고 조회")
    @GetMapping(value = "/id/{recruit_id}")
    public Response<RecruitDto> getRecruitDetail(
        @PathVariable(name = "recruit_id") Long recruit_id) {

        RecruitDto recruitInfo = recruitService.getRecruitInfo(recruit_id);
        return Response.success(200, "공고 목록이 조회되었습니다", recruitInfo);
    }

    @Operation(summary = "채용 공고 삭제")
    @DeleteMapping(value = "delete/id/{recruit_id}")
    public Response<Void> deleteRecruitDetail(
        @PathVariable(name = "recruit_id") Long recruit_id) {

        recruitService.deleteRecruit(recruit_id);
        return Response.success(200, "공고 목록이 삭제되었습니다.");
    }

    @Operation(summary = "채용 공고 기술 수정")
    @PutMapping(value = "update/id/{recruit_id}")
    public Response<RecruitDto> updateRecruit(
        @PathVariable(name = "recruit_id") Long recruit_id,
        @RequestParam(name ="tech",required = true) String[] techs) throws NotAllowedValueException {
        
        RecruitDto recruitInfo = recruitService.updateRecruitTechs(recruit_id,techs);
        return Response.success(200, "공고 스택이 변경되었습니다.",recruitInfo);
    }


    @Operation(summary = "전체 채용 공고 조회")
    @GetMapping(value = "/list")
    public Response<HashMap<String,Object>> getRecruits(
        @RequestParam(name ="company", required = false, defaultValue = "*") String[] companies,
        @RequestParam(name ="jobCategory",required = false) String[] jobCategories,
        @RequestParam(name ="tech",required = false) String[] techs,
        @RequestParam(name ="pageNo" ,required = false) Integer pageNo,
        @RequestParam(name= "pageSize",required = false) Integer pageSize) throws NoResultException, ValidationException {

        List<RecruitDto> recruitList = recruitService.getRecruitList(companies,jobCategories,techs,pageNo,pageSize);
        HashMap<String,Object> result = new HashMap<>();
        // Singleton 데이터 조회
        JobTotalCntSingleton jobTotalCntSingleton = JobTotalCntSingleton.getInstance();
        
        result.put("recruitList", recruitList);
        result.put("totalCount", jobTotalCntSingleton.getTotalCnt());

        return Response.success(200, "공고 목록이 조회되었습니다", result);
    }

    @Data
    static class recruitRequest {
        @NotNull
        @Schema(description = "Url", example = "https://toss.im/career/job-detail?job_id=4071141003&company=%ED%86%A0%EC%8A%A4%EB%B1%85%ED%81%AC&gh_src=a6133a833us&utm_source=offline_conference&utm_medium=banner&utm_campaign=2307_tossbank_recruit", type = "String")
        private String url;

        @Schema(description = "회사명", example = "toss", type = "String")
        private String company;

        @Schema(description = "직군", example = "Backend", type = "String")
        private String occupation;
    }
}
