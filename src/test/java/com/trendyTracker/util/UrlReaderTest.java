package com.trendyTracker.util;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UrlReaderTest {

    @Test
    @DisplayName("url 정보 파싱")
    public void getUrlContent() throws Exception {
        // given
        var testUrl = "https://www.owl-dev.me/blog/62";

        // when
        Set<String> urlContent = UrlReader.getUrlContent(testUrl);

        // then
        Assertions.assertThat(urlContent.contains("Java")).isTrue();
        Assertions.assertThat(urlContent.contains("Python")).isTrue();
        

    }
}
