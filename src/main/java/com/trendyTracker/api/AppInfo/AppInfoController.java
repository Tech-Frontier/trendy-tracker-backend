package com.trendyTracker.api.AppInfo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendyTracker.common.config.Loggable;
import com.trendyTracker.common.response.Response;
import com.trendyTracker.service.AppInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Loggable
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


    @Operation(summary = "서비스 약관 조회")
    @GetMapping("/get-term")
    public Response<String> getAppTerms() {
        String appTerm = appInfoService.getAppInfo().getTerms();

        return Response.success(200, "약관이 정상 조회되었습니다", appTerm);
    }
}
