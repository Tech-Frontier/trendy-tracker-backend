package com.trendyTracker.util;

import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.trendyTracker.Job.domain.Tech;

@SpringBootTest
public class UrlReaderTest {

    @Test
    @DisplayName("url 정보 파싱")
    public void getUrlContent() throws Exception {
        // given
        var testUrl = "https://www.owl-dev.me/blog/62";

        // when
        Set<Tech> techs = UrlReader.getUrlContent(testUrl);

        // then
        List<String> techList = TechUtils.getTechNameList(techs);
        Assertions.assertThat(techList.contains("Java")).isTrue();
        Assertions.assertThat(techList.contains("Python")).isTrue();
    }
}
