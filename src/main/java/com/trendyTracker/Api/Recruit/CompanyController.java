package com.trendytracker.Api.Recruit;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendytracker.Common.Exception.ExceptionDetail.NoResultException;
import com.trendytracker.Common.Logging.Loggable;
import com.trendytracker.Common.Response.Response;
import com.trendytracker.Domain.Jobs.Companies.Company;
import com.trendytracker.Domain.Jobs.Companies.CompanyService;
import com.trendytracker.Domain.Jobs.Companies.Vo.CompanyInfo;
import com.trendytracker.Util.CompanyUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Loggable
@Tag(name = "Company", description = "회사 관리")
@RequestMapping("/api/company")
@RequiredArgsConstructor
@RestController
public class CompanyController {
    @Autowired
    private final CompanyService companyService;

    @Operation(summary = "회사 등록")
    @PostMapping(value = "/regist")
    public Response<Void> registCompany(
        @RequestBody @Validated registCompanyRequest registRequest,
        HttpServletRequest request, HttpServletResponse response
    ){
        var group = registRequest.companyGroup;
        var company = registRequest.companyName;
        var category = CompanyUtils.makeCompanyCategory(registRequest.companyCategory);

        if(companyService.isExist(company))
            return Response.fail(400, "해당 회사명은 이미 존재합니다");

        companyService.regist(new CompanyInfo(group, category, company));

        addHeader(request, response);
        return Response.success(200, "정상적으로 회사가 등록되었습니다.");
    }

    @Operation(summary = "회사 규모 변경")
    @PutMapping(value = "/update/category")
    public Response<CompanyInfo> updateCompanyCategory(
        updateCategoryRequest updateRequest,
        HttpServletRequest request, HttpServletResponse response
    ){
        var companyName = updateRequest.company;
        var category = CompanyUtils.makeCompanyCategory(updateRequest.companyCategory);
        
        Company company = companyService.updateCategory(companyName, category);
        var companyInfo = new CompanyInfo(company.getCompanyGroup(),
                                    company.getCompanyCategory(),
                                    company.getCompanyName());
        
        addHeader(request, response);
        return Response.success(200, "회사 규모를 변경했습니다",companyInfo);        
    }

    @Operation(summary = "회사 그룹 변경")
    @PutMapping(value = "/update/group")
    public Response<CompanyInfo> updateCompanyGroup(
        updateGroupRequest updateRequest,
        HttpServletRequest request, HttpServletResponse response
    ){
        var companyName = updateRequest.company;
        var group = updateRequest.companyGroup;

        Company company = companyService.updateGroup(companyName, group);
        var companyInfo = new CompanyInfo(company.getCompanyGroup(),
                                    company.getCompanyCategory(),
                                    company.getCompanyName());

        addHeader(request, response);
        return Response.success(200, "회사 그룹을 변경했습니다",companyInfo);     
    }

    @Operation(summary = "회사 전체 조회")
    @GetMapping(value = "/list/all")
    public Response<List<CompanyInfo>> getCompanyList(HttpServletRequest request, HttpServletResponse response) throws NoResultException{
        List<CompanyInfo> companyList = companyService.getCompanyList();

        addHeader(request, response);
        return Response.success(200, "정상적으로 조회되었습니다",companyList);
    }

    @Operation(summary = "그룹사 별 회사 조회")
    @GetMapping(value = "/list/group")
    public Response<List<CompanyInfo>> getCompanyGroupList(
        @RequestBody @Validated GroupCompanyRequest groupRequest,
        HttpServletRequest request, HttpServletResponse response
    ) throws NoResultException{
        var group = groupRequest.companyGroup;
        List<CompanyInfo> companyGroup = companyService.getCompanyGroupList(group);

        addHeader(request, response);
        return Response.success(200, "정상적으로 조회되었습니다",companyGroup);
    }

    
    private String addHeader(HttpServletRequest request, HttpServletResponse response) {
        UUID uuid = (UUID) request.getAttribute("uuid");
        response.addHeader("uuid", uuid.toString());
        return uuid.toString();
    }

    @Data
    static class registCompanyRequest {
        @NotNull
        @Schema(description = "회사 그룹", example = "kakao", type = "String")
        private String companyGroup;

        @NotNull
        @Schema(description = "회사 규모", example = "Series_PreA, Series_A, Series_B, Series_C, Series_D, Listed", type = "String")
        private String companyCategory;    
        
        @NotNull
        @Schema(description = "회사 명", example = "kakao style", type = "String")
        private String companyName;
    }

    @Data
    static class updateCategoryRequest {
        @NotNull
        @Schema(description = "회사명", example = "toss", type = "String")
        private String company;

        @NotNull
        @Schema(description = "회사 규모", example = "Series_PreA, Series_A, Series_B, Series_C, Series_D, Listed", type = "String")
        private String companyCategory;    
    }

    @Data
    static class updateGroupRequest {
        @NotNull
        @Schema(description = "회사명", example = "toss", type = "String")
        private String company;

        @NotNull
        @Schema(description = "회사 그룹", example = "kakao", type = "String")
        private String companyGroup;    
    }

    @Data
    static class GroupCompanyRequest{
        @NotNull
        @Schema(description = "회사 그룹", example = "kakao", type = "String")
        private String companyGroup;    
    }
}
