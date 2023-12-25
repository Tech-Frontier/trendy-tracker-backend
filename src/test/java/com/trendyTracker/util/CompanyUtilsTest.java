package com.trendyTracker.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.trendyTracker.Domain.Jobs.Companies.Company.CompanyCategory;
import com.trendyTracker.Util.CompanyUtils;

public class CompanyUtilsTest {
    @Test
    void testMakeCompanyCategory() {
        // given
        String category = "Series_A";
        // when 
        CompanyCategory makeCompanyCategory = CompanyUtils.makeCompanyCategory(category);
        // then 
        assertEquals(makeCompanyCategory.name(), "Series_A");
    }

    @Test
    void testMakeCompanyCategoryWithOtherValue() {
        // given, when
        // then 
        assertThrows(IllegalArgumentException.class, () 
                        -> CompanyUtils.makeCompanyCategory(""));
        
    }
}
