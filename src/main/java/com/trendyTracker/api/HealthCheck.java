package com.trendyTracker.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "App Info", description = "Health Check")
@RequiredArgsConstructor
@RequestMapping("/api/health-check")
@RestController
public class HealthCheck {

    @Operation(tags = { "App Info" }, summary = "health check")
    @GetMapping("/")
    public String getHealthCheck() {
        return "ok";
    }
}
