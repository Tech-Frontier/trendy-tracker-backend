package com.trendyTracker.util;

import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UrlReaderTest {

    @Test
    @DisplayName("url 정보 파싱")
    public void getUrlContent() throws Exception {
        // given
        var testUrl = "https://www.owl-dev.me/blog/68";

        // when
        Set<String> urlContent = UrlReader.getUrlContent(testUrl);

        // then
        Assertions.assertThat(urlContent.contains("Substation API 성능 개선기 (ORM vs SQL) 2편"));
        Assertions.assertThat(urlContent.contains("SQL과 ORM에 대해서 파고들고,"));

    }
}
