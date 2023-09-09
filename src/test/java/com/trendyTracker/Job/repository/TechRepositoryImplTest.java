package com.trendyTracker.Job.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.trendyTracker.TrendyTrackerApplication;

import jakarta.transaction.Transactional;

@SpringBootTest(classes = TrendyTrackerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class TechRepositoryImplTest {
    @Autowired
    private TechRepository techRepository;

    @Test
    @DisplayName("Tech 등록 여부 체크 시, 대소문자 미 구분")
    void testIsTechRegist() {
        // given
        String upperCase = "JAVA";
        String lowerCase = "java";
        String camelCase = "Java";

        // when
        Boolean isUpperExist = techRepository.isTechRegist(upperCase);
        Boolean isLowerExist = techRepository.isTechRegist(lowerCase);
        Boolean isCamelExist = techRepository.isTechRegist(camelCase);

        // then
        Assertions.assertThat(isUpperExist).isTrue();
        Assertions.assertThat(isLowerExist).isTrue();
        Assertions.assertThat(isCamelExist).isTrue();
    }
}
