package com.trendyTracker.Api.Recruit;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trendyTracker.Common.Logging.Loggable;
import com.trendyTracker.Common.Response.Response;
import com.trendyTracker.Domain.Jobs.statistics.StaticsService;
import com.trendyTracker.Domain.Jobs.statistics.Dto.ChartInfo;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Loggable
@RequestMapping("/api/chart")
@RequiredArgsConstructor
@RestController
public class ChartController {
    @Autowired
    private final StaticsService chartService;

    @Operation(summary = "기간별 Tech 통계 조회")
    @GetMapping(value = "/techs")
    public Response<ChartInfo> getChartInfo (
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
        HttpServletRequest request, HttpServletResponse response 
    ){        
        ChartInfo techCharts = chartService.getTechCharts(fromDate, toDate);

        addHeader(request, response);
        return Response.success(200, "차트 조회를 완료했습니다",techCharts);
    }

    private String addHeader(HttpServletRequest request, HttpServletResponse response) {
        UUID uuid = (UUID) request.getAttribute("uuid");
        response.addHeader("uuid", uuid.toString());
        return uuid.toString();
    }
}
