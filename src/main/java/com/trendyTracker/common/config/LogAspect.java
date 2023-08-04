package com.trendyTracker.common.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAspect {
    // Loggable 어노테이션이 클래스에 적용되어 있는 경우에만
    @Pointcut("@within(Loggable)")
    public void loggableClass(){
    }

    // API
    @Before("loggableClass() && execution(* *(..))")
    public synchronized void logBeforeMethod(JoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        Object[] args = joinPoint.getArgs();
        var inputParms= args.length == 0 ? args.toString() : args[0].toString();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        logger.info("===================");
        logger.info("API 요청: " + request.getRequestURI());
        logger.info("메서드 인자: {}", inputParms);
    }

    // Exception 
    @AfterThrowing(pointcut = "loggableClass() && execution(* *(..))", throwing = "ex")
    public synchronized void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        Object[] args = joinPoint.getArgs();
        var inputParms= args.length == 0 ? args.toString() : args[0].toString();
        
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        logger.error("===================");
        logger.error("API 요청: " + request.getRequestURI());
        logger.error("메서드 인자: {}", inputParms);
        logger.error("예외 타입: " + ex.getClass().getSimpleName());
        logger.error("예외 발생: " + ex.getMessage());
    }

}
