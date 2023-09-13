package com.trendyTracker.util;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.trendyTracker.Job.dto.JobInfoDto;

@SpringBootTest
public class UrlReaderTest {

    @Test
    @DisplayName("url 정보 파싱")
    public void getUrlContent() throws Exception {
        // given
        var testUrl = "https://www.owl-dev.me/blog/62";

        // when
        JobInfoDto jobInfo = UrlReader.getUrlContent(testUrl);

        // then
        List<String> techList = TechUtils.getTechNameList(jobInfo.techSet());
        Assertions.assertThat(jobInfo.title().equals("Substation Logging System 적용기"));
        Assertions.assertThat(techList.contains("Java")).isTrue();
        Assertions.assertThat(techList.contains("Python")).isTrue();
    }
}
