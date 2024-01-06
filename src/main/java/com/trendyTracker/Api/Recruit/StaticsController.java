package com.trendyTracker.Api.Recruit;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trendyTracker.Common.Exception.ExceptionDetail.NoResultException;
import com.trendyTracker.Common.Logging.Loggable;
import com.trendyTracker.Common.Response.Response;
import com.trendyTracker.Domain.Jobs.Statistics.StaticsService;
import com.trendyTracker.Domain.Jobs.Statistics.Dto.ChartInfo;
import com.trendyTracker.Domain.Jobs.Statistics.Dto.TrendInfo;
import com.trendyTracker.Domain.Jobs.Techs.Tech.TechType;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Loggable
@RequestMapping("/api/chart")
@RequiredArgsConstructor
@RestController
public class StaticsController {
    @Autowired
    private final StaticsService chartService;

    @Operation(summary = "기간별 채용 기술스택 통계 조회")
    @GetMapping(value = "/periods")
    public Response<ChartInfo> getChartInfo (
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
        @RequestParam TechType techType,
        HttpServletRequest request, HttpServletResponse response 
    ){        
        ChartInfo techCharts = chartService.getTechCharts(fromDate, toDate, techType);

        addHeader(request, response);
        return Response.success(200, "차트 조회를 완료했습니다",techCharts);
    }

    @Operation(summary = "기술 스택별 월별 채용 공고 점유율 조회")
    @GetMapping(value = "/tech")
    public Response<List<TrendInfo>> getChartInfoByTechName(
        @RequestParam String techName,
        HttpServletRequest request, HttpServletResponse response
    ) throws NoResultException{
        List<TrendInfo> trendInfo = chartService.getTrends(techName);

        addHeader(request, response);
        return Response.success(200, "차트 조회를 완료했습니다",trendInfo);
    }

    private String addHeader(HttpServletRequest request, HttpServletResponse response) {
        UUID uuid = (UUID) request.getAttribute("uuid");
        response.addHeader("uuid", uuid.toString());
        return uuid.toString();
    }
}
