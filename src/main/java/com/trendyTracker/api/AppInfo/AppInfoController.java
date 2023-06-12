package com.trendyTracker.api.AppInfo;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendyTracker.common.response.Response;
import com.trendyTracker.service.AppInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Tag(name = "App Info", description = "앱 정보 API")
@RequiredArgsConstructor
@RequestMapping("/api/appInfo")
@RestController
public class AppInfoController {
    private final AppInfoService appInfoService;

    @Operation(summary = "health check")
    @GetMapping("/health-check")
    public String getHealthCheck() {
        return "ok";
    }

    @Operation(summary = "서비스 버전 조회")
    @GetMapping("/get-version")
    public Response<String> getAppVersion() {
        String appVersion = appInfoService.getAppInfo().getVersion();

        return Response.success(200, "엡 버전이 정상 조회되었습니다.", appVersion);
    }

    @Operation(summary = "서비스 약관 조회")
    @GetMapping("/get-term")
    public Response<String> getAppTerms() {
        String appTerm = appInfoService.getAppInfo().getTerms();

        return Response.success(200, "앱 약관이 정상 조회되었습니다", appTerm);
    }

    @Operation(summary = "서비스 정보 입력")
    @PostMapping("/save")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "앱 정보 등록이 완료되었습니다.") })
    public Response<Void> SaveAppInfo(@RequestBody @Validated appInfoRequest appInfoRequest) {
        String version = appInfoRequest.getVersion();
        String terms = appInfoRequest.getTerms();

        appInfoService.saveAppInfo(version, terms);

        return Response.success(200, "앱 정보 등록이 완료되었습니다.", null);
    }

    @Data
    static class appInfoRequest {
        @NotNull
        @Schema(description = "버전", example = "1.0", type = "String")
        private String version;

        @NotNull
        @Schema(description = "약관", example = "약관", type = "String")
        private String terms;

    }
}
