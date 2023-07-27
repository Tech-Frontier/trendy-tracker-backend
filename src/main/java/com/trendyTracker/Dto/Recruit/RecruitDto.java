package com.trendyTracker.Dto.Recruit;

import java.time.LocalDateTime;
import java.util.List;

import com.trendyTracker.domain.Job.Company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruitDto {
    private long id;

    @Schema(description = "회사명", example = "네이버")
    private String company;

    @Schema(description = "직군", example = "백엔드")
    private String occupation;

    @Schema(description = "채용 페이지", example = "https://naver.com")
    private String url;

    @Schema(description = "생성 시간", example = "YYYY-MM-DD HH24:mi:ss")
    private LocalDateTime createdTime;

    @Schema(description = "테크 항목", example = "[C#, Java]")
    private List<String> techList;
    

    public RecruitDto(long id, Company company, String occupation, String url, LocalDateTime createTime){
        this.id = id;
        this.company =company.getCompany_name();
        this.occupation = occupation;
        this.url = url;
        this.createdTime = createTime;
        this.techList = null;
    }
}
