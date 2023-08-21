package com.trendyTracker.common.config;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.bind.annotation.RequestParam;

import com.trendyTracker.common.kafka.KafkaProducer;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAspect {
    @Autowired
    private KafkaProducer kafkaProducer;

    // Loggable 어노테이션이 클래스에 적용되어 있는 경우에만
    @Pointcut("@within(Loggable)")
    public void loggableClass(){
    }

    // API
    @Before("loggableClass() && execution(* *(..))")
    public void logBeforeMethod(JoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        Parameter[] parameters = extractParamName(joinPoint);
        Object[] args = joinPoint.getArgs();
        String inputParms ="";

        for(int i =0; i< args.length; i++){
            String paramName ="";
            RequestParam annotation = parameters[i].getAnnotation(RequestParam.class);
            if(annotation != null) 
                paramName = annotation.name();

            String paramInfo = extractParamValue(paramName,args[i]);
            inputParms += paramInfo;
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        kafkaProducer.sendMessage("logger",request.getRequestURI(), inputParms);
        String message = String.format("\nAPI path: %s \nparams: %s", request.getRequestURI(), inputParms);
        logger.info(message);
    }


    // Exception 
    @AfterThrowing(pointcut = "loggableClass() && execution(* *(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        Parameter[] parameters = extractParamName(joinPoint);
        Object[] args = joinPoint.getArgs();
        String inputParms ="";

        for(int i =0; i< args.length; i++){
            String paramName ="";
            RequestParam annotation = parameters[i].getAnnotation(RequestParam.class);
            if(annotation != null) 
                paramName = annotation.name();

            String paramInfo = extractParamValue(paramName,args[i]);
            inputParms += paramInfo;
        }
        
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        inputParms += String.format("\n%s: %s",ex.getClass().getSimpleName(), ex.getMessage());

        kafkaProducer.sendMessage("error",request.getRequestURI(), inputParms);
        String message = String.format("\nAPI path: %s \nparams: %s", request.getRequestURI(), inputParms);
        logger.error(message);
    }

    private Parameter[] extractParamName(JoinPoint joinPoint) {
    /*
     * HTTP API request Params name 추출
     */
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Parameter[] parameters = method.getParameters();
        return parameters;
    }

    private String extractParamValue(String name, Object param) {
    /*
     * HTTP API request Params value 추출
     */
        String paramInfo = "";
        if (param == null)  return "";
            
        if (param instanceof String[]) {
            String[] arrayParam = (String[]) param;
            for (String value : arrayParam) 
                paramInfo += String.format("\n%s: %s",name, value);

        } else if (param instanceof String) {
            paramInfo += String.format("\n%s: %s",name, param);

        } else if (param instanceof Integer) {
            Integer intValue = (Integer) param;
            paramInfo += String.format("\n%s: %s",name, intValue.intValue());
        }

        return paramInfo;
    }


}
