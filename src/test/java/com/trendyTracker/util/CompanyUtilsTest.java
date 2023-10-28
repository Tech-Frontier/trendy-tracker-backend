package com.trendyTracker.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.trendyTracker.Job.domain.Company.CompanyCategory;

public class CompanyUtilsTest {
    @Test
    void testMakeCompanyCategory() {
        // given
        String category = "Series_A";
        // when 
        CompanyCategory makeCompanyCategory = CompanyUtils.makeCompanyCategory(category);
        // then 
        Assertions.assertThat(makeCompanyCategory.name()).isEqualTo("Series_A");
    }

    @Test
    void testMakeCompanyCategoryWithOtherValue() {
        // given, when
        // then 
        assertThrows(IllegalArgumentException.class, () 
                        -> CompanyUtils.makeCompanyCategory(""));
        
    }
}
