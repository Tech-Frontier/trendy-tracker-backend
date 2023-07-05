package com.trendyTracker.api.Recruit;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trendyTracker.Dto.Recruit.RecruitDto;
import com.trendyTracker.common.response.Response;
import com.trendyTracker.service.RecruitService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Tag(name = "recruit Url", description = "채용 공고")
@RequestMapping("/api/recruit")
@RequiredArgsConstructor
@RestController
public class RecruitController {

    private final RecruitService recruitService;

    @Operation(summary = "채용 공고 분석")
    @PostMapping(value = "/list")
    public Response<List<RecruitDto>> getList() {

        List<RecruitDto> recruitList = recruitService.getRecruitList();
        return Response.success(200, "공고 목록이 조회되었습니다", recruitList);
    }

    @Operation(summary = "공고 상세 페이지")
    @GetMapping(value = "/")
    public Response<RecruitDto> getRecruitDetail(@RequestParam(name = "id") @NotNull Long id) {

        RecruitDto recruitInfo = recruitService.getRecruitInfo(id);
        return Response.success(200, "공고 목록이 조회되었습니다", recruitInfo);
    }
}
