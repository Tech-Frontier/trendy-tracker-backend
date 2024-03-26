package com.trendytracker.api.appinfo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendytracker.common.logging.Loggable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Loggable
@Tag(name = "App Info", description = "앱 정보 API")
@RequiredArgsConstructor
@RequestMapping("/api/appInfo")
@RestController
public class AppInfoController {

    @Operation(summary = "health check")
    @GetMapping("/health-check")
    public String getHealthCheck() {
        return "ok";
    }
}
