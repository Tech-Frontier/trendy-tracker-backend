package com.trendyTracker.api.Tech;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendyTracker.Job.service.TechService;
import com.trendyTracker.common.Exception.ExceptionDetail.AlreadyExistException;
import com.trendyTracker.common.config.Loggable;
import com.trendyTracker.common.response.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Loggable
@Tag(name = "Tech", description = "기술 스택")
@RequestMapping("/api/tech")
@RestController
@AllArgsConstructor
public class TechController {

    private final TechService techService;

    @Operation(summary = "기술 스택 등록")
    @PostMapping(value = "/stack/create")
    public Response<String> registTechStack(@RequestBody @Validated techRequest request) throws AlreadyExistException {
        String registTechStack = techService.registTechStack(request.tech);

        return Response.success(200, "개발 스택이 등록되었습니다", registTechStack);
    }

    @Operation(summary = "기술 스택 제거")
    @DeleteMapping(value = "/stack/delete")
    public Response<String> deleteTechStack(@RequestBody @Validated techRequest request) {
        String deleteTechStack = techService.deleteTechStack(request.tech);

        return Response.success(200, "개발 스택이 제거되었습니다", deleteTechStack);
    }

    @Operation(summary = "기술 스택 목록 조회")
    @GetMapping(value = "/stack/list")
    public Response<List<String>> getTechStackList() {
        List<String> techList = techService.getTechList();

        return Response.success(200, "개발 스택 목록이 조회됐습니다", techList);
    }

    @Data
    static class techRequest {
        @NotNull
        @Schema(description = "기술", example = "C#", type = "String")
        private String tech;
    }

}
