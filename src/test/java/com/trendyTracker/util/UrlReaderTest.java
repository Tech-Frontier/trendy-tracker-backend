package com.trendyTracker.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.trendyTracker.Adaptors.CacheMemory.TechsCacheImpl;
import com.trendyTracker.Domain.Jobs.Recruits.Dto.JobScrapInfoDto;
import com.trendyTracker.Util.TechUtils;
import com.trendyTracker.Util.UrlReader;

@SpringBootTest
public class UrlReaderTest {
    @Autowired
    TechsCacheImpl techsCache;

    @Test
    @DisplayName("url 정보 파싱")
    public void getUrlContent() throws Exception {
        // given
        var testUrl = "https://www.owl-dev.me/blog/62";

        // when
        JobScrapInfoDto jobInfo = UrlReader.getUrlContent(testUrl, techsCache);

        // then
        List<String> techList = TechUtils.getTechNameList(jobInfo.techSet());
        assertEquals(jobInfo.title(), "Substation Logging System 적용기");
        assertTrue(techList.contains("Java"));
    }
}
