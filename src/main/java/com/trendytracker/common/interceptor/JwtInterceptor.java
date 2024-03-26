package com.trendytracker.common.interceptor;

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
    /**
     *  validate admin token 
     */
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtToken.equals(adminToken)) 
                return true;

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
    }
}
