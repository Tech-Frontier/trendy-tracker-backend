package com.trendyTracker.common.config.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterCeptorConfig implements WebMvcConfigurer{
    @Value("${token.value}")
    private String tokenValue;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // insert uuid at header
        registry.addInterceptor(new UUIDInterceptor());

        // validate jwt below api path
        registry.addInterceptor(new JwtInterceptor(tokenValue))
            // Tech
            .addPathPatterns("/api/tech/stack/create")
            .addPathPatterns("/api/tech/stack/delete")
            // Recruit
            .addPathPatterns("/api/recruit/regist")
            .addPathPatterns("/api/recruit/update")
            .addPathPatterns("/api/recruit/delete/id/*")
            .addPathPatterns("/api/recruit/update/id/*")
            // Company
            .addPathPatterns("/api/company/regist")
            .addPathPatterns("/api/company/update/category")
            .addPathPatterns("/api/company/update/group");
    }
}
