package com.trendyTracker.common.config;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {
    private String adminToken;

    public JwtInterceptor(String adminToken){
        this.adminToken = adminToken;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

        // JWT 토큰 검증 로직 구현
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);

            // Admin 토큰
            if (jwtToken.equals(adminToken)) return true;

            return false;
        }
        else {
            // JWT 토큰이 없는 경우 처리
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
    }
}
