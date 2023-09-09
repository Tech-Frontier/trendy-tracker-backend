package com.trendyTracker.common.config;

import java.util.UUID;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class UUIDInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse reponse, Object handler){
    /**
     *  uuid 를 생성해 헤더에 추가
     */
        UUID uuid = UUID.randomUUID();
        request.setAttribute("uuid", uuid);
        return true;
    }

}
