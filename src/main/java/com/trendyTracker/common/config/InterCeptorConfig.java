package com.trendyTracker.common.config;

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
        // JWT 토큰 검증을 적용할 URL 패턴 지정
        registry.addInterceptor(new JwtInterceptor(tokenValue))
            // Tech
            .addPathPatterns("/api/tech/stack/create")
            .addPathPatterns("/api/tech/stack/delete")
            // Recruit
            .addPathPatterns("/api/recruit/regist")
            .addPathPatterns("/api/recruit/delete/id/*")
            .addPathPatterns("/api/recruit/update/id/*");

    }
}
